package tournament_ranking.infrastructure

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.dynamodbv2.model.*
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.spec.UpdateTableSpec
import org.slf4j.LoggerFactory
import tournament_ranking.config.DynamoDBConfig
import com.amazonaws.services.dynamodbv2.model.TableDescription
import com.sun.corba.se.spi.presentation.rmi.StubAdapter.request
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import java.util.ArrayList
import com.amazonaws.services.dynamodbv2.model.ProjectionType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex


class DynamoDBFactory(private val awsClient: AmazonDynamoDBClient) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val tableName = DynamoDBConfig.competitorTableName
    companion object {
        const val gsiIndexName = "leaderBoardIndex"
        const val gsiPkName = "gsipk"
    }

    fun createTable() {
//        awsClient.deleteTable(DynamoDBConfig.competitorTableName)
        val gsi = createGSI()

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
                KeySchemaElement("pseudo", KeyType.HASH),
                KeySchemaElement("points", KeyType.RANGE)
            )
            .withProvisionedThroughput(
                ProvisionedThroughput(
                    10, 10
                )
            )
            .withTableName(tableName)
            .withGlobalSecondaryIndexes(gsi)

        try {
            awsClient.createTable(request)
        } catch(e: AmazonServiceException) {
            logger.error("DYNAMO: Table $tableName not created. $e")
        }
    }

    fun createGSI(): GlobalSecondaryIndex {
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