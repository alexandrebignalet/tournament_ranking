package tournament_ranking

import dagger.Module
import dagger.Provides
import io.dropwizard.setup.Environment
import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.repositories.CompetitorInMemoryRepository
import tournament_ranking.resources.CompetitorsResource
import javax.inject.Singleton

@Module
class TournamentRankingModule(private val env: String) {

    @Singleton
    @Provides
    fun provideCompetitorRepository(): CompetitorRepository {
        return CompetitorInMemoryRepository()
    }
}