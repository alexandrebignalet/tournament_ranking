package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.CompetitorsResource
import tournament_ranking.resources.DefaultResource

class TournamentRankingApp : Application<TournamentRankingConfig>() {

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        val competitorRepository = CompetitorRepository()
        val resources = listOf(DefaultResource(), CompetitorsResource(competitorRepository))

        resources.forEach { jersey.register(it) }
    }

}