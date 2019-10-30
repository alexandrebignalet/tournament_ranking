package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.resources.exception.ApiErrorExceptionMapper
import tournament_ranking.config.TournamentRankingConfig
import io.dropwizard.lifecycle.ServerLifecycleListener
import org.eclipse.jetty.server.Server
import org.slf4j.LoggerFactory


class TournamentRankingApp : Application<TournamentRankingConfig>() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        val tournamentRankingComponent: TournamentRankingComponent = DaggerTournamentRankingComponent.builder()
            .tournamentRankingModule(TournamentRankingModule(configuration))
            .build()

        val resources = listOf(
            tournamentRankingComponent.competitorResource(),
            ApiErrorExceptionMapper()
        )

        resources.forEach { jersey.register(it) }

        environment.lifecycle().addServerLifecycleListener(object : ServerLifecycleListener {
            override fun serverStarted(server: Server?) {
                assert(server != null)
                logger.info("Tournament Ranking started at {}", server!!.getURI())
            }
        })

    }

}