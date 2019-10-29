package tournament_ranking

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import dagger.Module
import dagger.Provides
import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.repositories.CompetitorInMemoryRepository
import javax.inject.Singleton
import com.amazonaws.auth.BasicAWSCredentials
import tournament_ranking.config.TournamentRankingConfig
import tournament_ranking.infrastructure.DynamoDBService
import tournament_ranking.repositories.CompetitorDynamoDBRepository

@Module
class TournamentRankingModule(private val configuration: TournamentRankingConfig) {

    @Singleton
    @Provides
    fun provideCompetitorRepository(): CompetitorRepository {
        return if (configuration.mode == "dynamodb") {
            dynamoDBFactory().createTable()
            CompetitorDynamoDBRepository(amazonDynamoDBClient(), configuration.competitorsTableName)
        } else {
            CompetitorInMemoryRepository()
        }
    }

    @Singleton
    @Provides
    fun dynamoDBFactory(): DynamoDBService {
        return DynamoDBService(amazonDynamoDBClient(), configuration.competitorsTableName)
    }

    fun amazonDynamoDBClient(): AmazonDynamoDBClient {
        val client = AmazonDynamoDBClient(provideAmazonAwsCredentials())
        client.setEndpoint(configuration.endpointURL)
        return client
    }

    private fun provideAmazonAwsCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(configuration.accessKey, configuration.accessSecret)
    }
}