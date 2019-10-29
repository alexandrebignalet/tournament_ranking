package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.domain.Competitor
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.CompetitorsResource
import tournament_ranking.resources.exception.ApiErrorExceptionMapper

class TournamentRankingApp : Application<TournamentRankingConfig>() {

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        val competitorRepository = CompetitorRepository()

        competitorRepository.add(Competitor("alex", 10))
        competitorRepository.add(Competitor("michel", 20))
        competitorRepository.add(Competitor("hugues", 30))

        val resources = listOf(
            CompetitorsResource(competitorRepository),

            ApiErrorExceptionMapper()
        )

        resources.forEach { jersey.register(it) }
    }

}