package tournament_ranking

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.CompetitorsResource
import tournament_ranking.resources.DefaultResource
import tournament_ranking.resources.exception.ApiErrorExceptionMapper

class TournamentRankingApp : Application<TournamentRankingConfig>() {

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        val competitorRepository = CompetitorRepository()

        val resources = listOf(
            DefaultResource(),
            CompetitorsResource(competitorRepository),

            ApiErrorExceptionMapper()
        )

        resources.forEach { jersey.register(it) }
    }

}