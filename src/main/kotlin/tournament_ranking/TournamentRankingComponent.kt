package tournament_ranking

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import dagger.Component
import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.resources.CompetitorsResource
import javax.inject.Singleton

@Singleton
@Component(modules = [TournamentRankingModule::class])
interface TournamentRankingComponent {

    fun competitorRepository(): CompetitorRepository

    fun competitorResource(): CompetitorsResource
}