package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.resources.exception.ApiErrorExceptionMapper
import tournament_ranking.config.TournamentRankingConfig
import io.dropwizard.lifecycle.ServerLifecycleListener
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.slf4j.LoggerFactory
import java.util.*
import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration


class TournamentRankingApp : Application<TournamentRankingConfig>() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        configureCors(environment)

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

    private fun configureCors(environment: Environment) {
        val cors = environment.servlets().addFilter("CORS", CrossOriginFilter::class.java)

        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
    }

}