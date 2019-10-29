package tournament_ranking.repositories

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.*
import tournament_ranking.domain.CompetitorRepository
import java.util.HashMap
import com.amazonaws.services.dynamodbv2.model.*
import tournament_ranking.config.DynamoDBConfig
import tournament_ranking.domain.Competitor
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import tournament_ranking.infrastructure.DynamoDBService
import java.math.BigDecimal

class CompetitorDynamoDBRepository(private val dynamoClient: AmazonDynamoDBClient,
                                   private val config: DynamoDBConfig): CompetitorRepository {

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
            .withTableName(config.competitorsTableName)
            .withKeyConditions(keyConditions)
            .withScanIndexForward(false)

        val result = dynamoClient.query(query)

        return result.items.count() > 0
    }

    override fun get(competitorId: String): Competitor? {
        return rankList().find { it.pseudo == competitorId }
    }

    override fun save(competitor: Competitor) {
        if (exists(competitor.pseudo)) {
            update(competitor)
        } else {
            val itemValues = HashMap<String, AttributeValue>()
            itemValues[PSEUDO_KEY] = AttributeValue(competitor.pseudo)
            itemValues[POINTS_KEY] = AttributeValue().withN(competitor.points.toString())
            itemValues[DynamoDBService.gsiPkName] = AttributeValue(GSI_VALUE)

            dynamoClient.putItem(config.competitorsTableName, itemValues)
        }
    }

    private fun update(competitor: Competitor) {
        val identifier = HashMap<String, AttributeValue>()
        identifier[PSEUDO_KEY] = AttributeValue(competitor.pseudo);

        val updateValues = HashMap<String, AttributeValueUpdate>()

        updateValues[POINTS_KEY] = AttributeValueUpdate(
            AttributeValue().withN(competitor.points.toString()),
            AttributeAction.PUT)

        dynamoClient.updateItem(config.competitorsTableName, identifier, updateValues)
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
        if (hashKeysToDelete.isEmpty()) return;

        val competitorTableWriteItems = TableWriteItems(config.competitorsTableName)
            .withHashOnlyKeysToDelete(PSEUDO_KEY, *hashKeysToDelete)

        dynamoDb.batchWriteItem(competitorTableWriteItems)
    }

    private fun leaderBoardIndex(): ItemCollection<QueryOutcome> {
        val table = dynamoDb.getTable(config.competitorsTableName)
        val index = table.getIndex(DynamoDBService.gsiIndexName)

        val querySpec = QuerySpec()
            .withHashKey(DynamoDBService.gsiPkName, GSI_VALUE)
            .withScanIndexForward(false)

        return index.query(querySpec)
    }
}