package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.exception.ApiError
import tournament_ranking.resources.exception.ApiErrorExceptionMapper
import tournament_ranking.resources.exception.CompetitorPseudoAlreadyUsed
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.Before
import tournament_ranking.domain.Competitor
import tournament_ranking.resources.dto.UpdateCompetitorPoints
import tournament_ranking.resources.exception.CompetitorNotFound


class CompetitorsResourceTest {
    private val apiErrorMessage = "{\"errors\":[\"pseudo est obligatoire\"]}"
    private val apiErrorStatus = 422

    @Test
    fun shouldAddACompetitorIfPseudoIsProvided() {
        val pseudo = "Jack"
        val response = doAddCompetitorRequest(pseudo)

        assertThat(response.status).isEqualTo(201)

        val createdCompetitor = repository.get(pseudo)
        assertThat(createdCompetitor).isNotNull
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
    fun shouldUpdateCompetitorPoints() {
        val competitor = Competitor("pseudo")
        repository.add(competitor)

        val points = 100
        val response = doUpdateCompetitorPointsRequest(competitor.id(), points)

        val updatedCompetitor = repository.get(competitor.id())
        assertThat(updatedCompetitor?.points).isEqualTo(points)
        assertThat(response.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)
    }

    private fun doUpdateCompetitorPointsRequest(pseudo: String, points: Int): Response {
        val command = UpdateCompetitorPoints(points)
        return resources.target("/tournament/competitors/$pseudo").request().put(Entity.json(command))
    }

    private fun doAddCompetitorRequest(pseudo: String?): Response {
        val command = AddCompetitor(pseudo)
        return resources.target("/tournament/competitors").request().post(Entity.json(command))
    }

    companion object {
        val repository = CompetitorRepository()

        val kotlinJacksonMapper = ObjectMapper().registerModule(KotlinModule())

        @ClassRule
        @JvmField
        val resources = ResourceTestRule.builder()
            .addResource(CompetitorsResource(repository))
            .addProvider(ApiErrorExceptionMapper())
            .setMapper(kotlinJacksonMapper)
            .build()
    }
}