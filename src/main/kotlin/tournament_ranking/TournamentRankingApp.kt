package tournament_ranking

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import tournament_ranking.resources.exception.ApiErrorExceptionMapper
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import tournament_ranking.config.TournamentRankingConfig


class TournamentRankingApp : Application<TournamentRankingConfig>() {

    override fun initialize(bootstrap: Bootstrap<TournamentRankingConfig>) {
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(
            bootstrap.configurationSourceProvider,
            EnvironmentVariableSubstitutor(false)
        )
    }

    override fun run(configuration: TournamentRankingConfig, environment: Environment) {
        println("Running ${configuration.name}")

        val jersey = environment.jersey()

        val mode: String = configuration.mode

        val tournamentRankingComponent: TournamentRankingComponent = DaggerTournamentRankingComponent.builder()
            .tournamentRankingModule(TournamentRankingModule(mode, configuration.dynamoDbConfig()))
            .build()

        val resources = listOf(
            tournamentRankingComponent.competitorResource(),
            ApiErrorExceptionMapper()
        )

        resources.forEach { jersey.register(it) }
    }

}