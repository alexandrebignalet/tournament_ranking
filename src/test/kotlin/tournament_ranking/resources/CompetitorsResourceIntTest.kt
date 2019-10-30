package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.exception.ApiError
import tournament_ranking.resources.exception.ApiErrorExceptionMapper
import tournament_ranking.resources.exception.CompetitorPseudoAlreadyUsed
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tournament_ranking.DaggerTournamentRankingComponent
import tournament_ranking.TournamentRankingModule
import tournament_ranking.config.TournamentRankingConfig
import tournament_ranking.domain.Competitor
import tournament_ranking.resources.dto.CompetitorWithRank
import tournament_ranking.resources.dto.ChangeCompetitorPoints
import tournament_ranking.resources.exception.CompetitorNotFound
import javax.ws.rs.core.GenericType
import io.dropwizard.configuration.YamlConfigurationFactory
import io.dropwizard.jackson.Jackson
import io.dropwizard.jersey.validation.Validators
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.configuration.SubstitutingSourceProvider



class CompetitorsResourceTest {
    private val apiErrorMessage = "{\"errors\":[\"pseudo est obligatoire\"]}"
    private val apiErrorStatus = 422

    val configuration = loadTestConfig()
    val appComponent = DaggerTournamentRankingComponent.builder()
        .tournamentRankingModule(TournamentRankingModule(configuration))
        .build()
    val repository = appComponent.competitorRepository()

    val kotlinJacksonMapper = ObjectMapper().registerModule(KotlinModule())

    @Rule
    @JvmField
    val resources = ResourceTestRule.builder()
        .addResource(CompetitorsResource(repository))
        .addProvider(ApiErrorExceptionMapper())
        .setMapper(kotlinJacksonMapper)
        .build()

    @Before
    fun emptyDB() {
        repository.reset()
    }

    @Test
    fun shouldAddACompetitorIfPseudoIsProvided() {
        val pseudo = "Jack"
        val response = doAddCompetitorRequest(pseudo)

        assertThat(response.status).isEqualTo(201)

        val createdCompetitor = repository.get(pseudo)
        assertThat(createdCompetitor).isNotNull()
    }

    @Test
    fun shouldThrowOnAddCompetitorIfPseudoIsNull() {
        val pseudo = null
        val response = doAddCompetitorRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)

        assertThat(response.readEntity(String::class.java)).isEqualTo(apiErrorMessage)
    }

    @Test
    fun shouldThrowOnAddCompetitorIfPseudoIsEmpty() {
        val pseudo = ""
        val response = doAddCompetitorRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)

        assertThat(response.readEntity(String::class.java)).isEqualTo(apiErrorMessage)
    }

    @Test
    fun shouldThrowOnAddCompetitorIfPseudoIsAlreadyRegistered() {
        val pseudo = "competitor"
        doAddCompetitorRequest(pseudo)

        val response = doAddCompetitorRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)
        assertThat(response.readEntity(ApiError::class.java)).isEqualTo(ApiError(CompetitorPseudoAlreadyUsed(pseudo).message!!))
    }

    @Test
    fun shouldThrowOnUpdateCompetitorPointsIfCompetitorDoesNotExist() {
        val aPseudo = "another_pseudo"
        val response = doUpdateCompetitorPointsRequest(aPseudo, 100)

        assertThat(response.status).isEqualTo(Response.Status.NOT_FOUND.statusCode)
        assertThat(response.readEntity(ApiError::class.java)).isEqualTo(ApiError(CompetitorNotFound(aPseudo).message!!))
    }

    @Test
    fun shouldIncreaseCompetitorPoints() {
        val competitor = Competitor("pseudo")
        repository.save(competitor)

        val points = 100
        val response = doUpdateCompetitorPointsRequest(competitor.id(), points)

        val updatedCompetitor = repository.get(competitor.id())
        assertThat(updatedCompetitor?.points).isEqualTo(points)
        assertThat(response.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    fun shouldDecreaseCompetitorPoints() {
        val initialPoints = 1000
        val competitor = Competitor("pseudo", initialPoints)
        repository.save(competitor)

        val points = -100
        val response = doUpdateCompetitorPointsRequest(competitor.id(), points)

        val updatedCompetitor = repository.get(competitor.id())
        assertThat(updatedCompetitor?.points).isEqualTo(initialPoints + points)
        assertThat(response.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    fun shouldQueryTheCompetitorsSortedByScore() {
        val (first, second, third) = initRepositoryWithSomeCompetitors()

        val response = resources.target("/tournament/competitors").request().get()

        assertThat(response.status).isEqualTo(Response.Status.OK.statusCode)
        assertThat(response.readEntity(object: GenericType<List<Competitor>>() {})).containsExactly(
            Competitor(first.pseudo, first.points),
            Competitor(second.pseudo, second.points),
            Competitor(third.pseudo, third.points)
        )
    }

    @Test
    fun shouldQueryACompetitorWithItsRank() {
        val (first) = initRepositoryWithSomeCompetitors()

        val response = resources.target("/tournament/competitors/${first.pseudo}").request().get()

        assertThat(response.status).isEqualTo(Response.Status.OK.statusCode)
        assertThat(response.readEntity(CompetitorWithRank::class.java)).isEqualTo(CompetitorWithRank(first.pseudo, first.points, 1))

    }

    @Test
    fun shouldDeleteAllTournamentCompetitors() {
        initRepositoryWithSomeCompetitors()

        val deleteResponse = resources.target("/tournament/competitors").request().delete()

        assertThat(deleteResponse.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)

        val getAllResponse = resources.target("/tournament/competitors").request().get()

        assertThat(getAllResponse.readEntity(object: GenericType<List<Competitor>>() {})).isEqualTo(emptyList<Competitor>())
    }

    private fun initRepositoryWithSomeCompetitors(): Triple<Competitor, Competitor, Competitor> {
        val first = Competitor("competitor1", 30)
        val second = Competitor("competitor2", 20)
        val third = Competitor("competitor3", -10)

        listOf(first, second, third).forEach { repository.save(it) }

        return Triple(first, second, third)
    }

    private fun doUpdateCompetitorPointsRequest(pseudo: String, points: Int): Response {
        val command = ChangeCompetitorPoints(points)
        return resources.target("/tournament/competitors/$pseudo").request().put(Entity.json(command))
    }

    private fun doAddCompetitorRequest(pseudo: String?): Response {
        val command = AddCompetitor(pseudo)
        return resources.target("/tournament/competitors").request().post(Entity.json(command))
    }

    private fun loadTestConfig(): TournamentRankingConfig {
        val objectMapper = Jackson.newObjectMapper()
        val validator = Validators.newValidator()
        val factory = YamlConfigurationFactory<TournamentRankingConfig>(TournamentRankingConfig::class.java, validator, objectMapper, "dw")

        return factory.build(
            SubstitutingSourceProvider(
                ResourceConfigurationSourceProvider(),
                EnvironmentVariableSubstitutor(false)
            ), "config-test.yaml")
    }
}