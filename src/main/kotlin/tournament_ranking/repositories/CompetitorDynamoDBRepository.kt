package tournament_ranking.repositories

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.*
import tournament_ranking.domain.CompetitorRepository
import java.util.HashMap
import com.amazonaws.services.dynamodbv2.model.*
import tournament_ranking.config.DynamoDBConfig
import tournament_ranking.domain.Competitor
import tournament_ranking.infrastructure.DynamoDBFactory
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import java.math.BigDecimal

class CompetitorDynamoDBRepository(private val dynamoClient: AmazonDynamoDBClient): CompetitorRepository {

    private val dynamoDb = DynamoDB(dynamoClient)

    companion object {
        const val PSEUDO_KEY = "pseudo"
        const val POINTS_KEY = "points"
        const val GSI_VALUE = "competitor"
    }

    override fun exists(competitorId: String): Boolean {
        val keyConditions = HashMap<String, Condition>()
        keyConditions[PSEUDO_KEY] = Condition()
            .withComparisonOperator(ComparisonOperator.EQ)
            .withAttributeValueList(
                AttributeValue(competitorId)
            )
        val query = QueryRequest()
            .withTableName(DynamoDBConfig.competitorTableName)
            .withKeyConditions(keyConditions)
            .withScanIndexForward(false)

        val result = dynamoClient.query(query)

        return result.items.count() > 0
    }

    override fun get(competitorId: String): Competitor? {
        return rankList().find { it.pseudo == competitorId }
    }

    override fun save(competitor: Competitor) {
        val itemValues = HashMap<String, AttributeValue>()
        itemValues[PSEUDO_KEY] = AttributeValue(competitor.pseudo)
        itemValues[POINTS_KEY] = AttributeValue().withN(competitor.points.toString())
        itemValues[DynamoDBFactory.gsiPkName] = AttributeValue(GSI_VALUE)

        if (exists(competitor.pseudo)) {
            dynamoClient.updateItem(DynamoDBConfig.competitorTableName, itemValues, HashMap())
        } else {
            dynamoClient.putItem(DynamoDBConfig.competitorTableName, itemValues)
        }
    }

    override fun rankList(): List<Competitor> {
        val items = leaderBoardIndex()

        return items.map {
            val pseudo: String = it.get(PSEUDO_KEY) as String
            val points: Int = (it.get(POINTS_KEY) as BigDecimal).intValueExact()
            Competitor(pseudo, points)
        }
    }

    override fun reset() {
        val hashKeysToDelete = leaderBoardIndex().map { it.get(PSEUDO_KEY) as String }.toTypedArray()
        val competitorTableWriteItems = TableWriteItems(DynamoDBConfig.competitorTableName)
            .withHashOnlyKeysToDelete(PSEUDO_KEY, *hashKeysToDelete)

        dynamoDb.batchWriteItem(competitorTableWriteItems)
    }

    private fun leaderBoardIndex(): ItemCollection<QueryOutcome> {
        val table = dynamoDb.getTable(DynamoDBConfig.competitorTableName)
        val index = table.getIndex(DynamoDBFactory.gsiIndexName)

        val querySpec = QuerySpec()
            .withHashKey(DynamoDBFactory.gsiPkName, GSI_VALUE)
            .withScanIndexForward(false)

        return index.query(querySpec)
    }

}