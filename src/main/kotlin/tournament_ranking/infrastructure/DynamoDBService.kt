package tournament_ranking.infrastructure

import com.amazonaws.services.dynamodbv2.model.*
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import tournament_ranking.config.DynamoDBConfig
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import java.util.ArrayList
import com.amazonaws.services.dynamodbv2.model.ProjectionType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex


class DynamoDBService(private val awsClient: AmazonDynamoDBClient,
                      private val config: DynamoDBConfig) {

    companion object {
        const val gsiIndexName = "leaderBoardIndex"
        const val gsiPkName = "gsipk"
    }

    fun createTable() {
        val request = CreateTableRequest()
            .withAttributeDefinitions(
                AttributeDefinition(
                    "pseudo", ScalarAttributeType.S
                ),
                AttributeDefinition(
                    "points", ScalarAttributeType.N
                ),
                AttributeDefinition(
                    gsiPkName, ScalarAttributeType.S
                )
            )
            .withKeySchema(
                KeySchemaElement("pseudo", KeyType.HASH)
            )
            .withProvisionedThroughput(
                ProvisionedThroughput(
                    10, 10
                )
            )
            .withTableName(config.competitorsTableName)
            .withGlobalSecondaryIndexes(createGSI())

        awsClient.createTable(request)
    }

    fun deleteTable() {
        awsClient.deleteTable(config.competitorsTableName)
    }

    private fun createGSI(): GlobalSecondaryIndex {
        val leaderBoardIndex = GlobalSecondaryIndex()
            .withIndexName(gsiIndexName)
            .withProvisionedThroughput(
                ProvisionedThroughput()
                    .withReadCapacityUnits(10.toLong())
                    .withWriteCapacityUnits(1.toLong())
            )
            .withProjection(Projection().withProjectionType(ProjectionType.ALL))

        val indexKeySchema = ArrayList<KeySchemaElement>()

        indexKeySchema.add(
            KeySchemaElement()
                .withAttributeName(gsiPkName)
                .withKeyType(KeyType.HASH)
        )

        indexKeySchema.add(
            KeySchemaElement()
                .withAttributeName("points")
                .withKeyType(KeyType.RANGE)
        )

        leaderBoardIndex.setKeySchema(indexKeySchema)

        return leaderBoardIndex
    }
}