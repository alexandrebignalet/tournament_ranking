package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.resources.DefaultResource

class TournamentRankingApp : Application<TournamentRankingConfig>() {

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val defaultResource = DefaultResource()
        environment.jersey().register(defaultResource)
    }

}