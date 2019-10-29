package tournament_ranking

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import dagger.Module
import dagger.Provides
import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.repositories.CompetitorInMemoryRepository
import javax.inject.Singleton
import com.amazonaws.auth.BasicAWSCredentials
import tournament_ranking.config.DynamoDBConfig
import tournament_ranking.infrastructure.DynamoDBFactory
import tournament_ranking.repositories.CompetitorDynamoDBRepository

@Module
class TournamentRankingModule(private val mode: String, private val configuration: DynamoDBConfig) {

    @Singleton
    @Provides
    fun provideCompetitorRepository(): CompetitorRepository {
        return if (mode == "dynamodb") {
            CompetitorDynamoDBRepository(provideAmazonDynamoDBClient())
        } else {
            CompetitorInMemoryRepository()
        }
    }


    @Provides
    fun provideAmazonDynamoDBClient(): AmazonDynamoDBClient {
        val client = AmazonDynamoDBClient(amazonAwsCredentials())
        client.setEndpoint(configuration.endpointURL)

        DynamoDBFactory(client).createTable()

        return client
    }

    private fun amazonAwsCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(configuration.accessKey, configuration.accessSecret)
    }
}