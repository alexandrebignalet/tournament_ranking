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
import com.fasterxml.jackson.datatype.guava.GuavaModule
import io.dropwizard.jackson.Jackson
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule


class CompetitorsResourceTest {
    private val apiErrorMessage = "{\"errors\":[\"pseudo est obligatoire\"]}"
    private val apiErrorStatus = 422

    @Test
    fun shouldAddACompetitorWithHisPseudo() {
        val pseudo = "Jack"
        val response = doRequest(pseudo)

        assertThat(response.status).isEqualTo(201)

        val createdCompetitor = repository.get(pseudo)
        assertThat(createdCompetitor).isNotNull
    }

    @Test
    fun shouldThrowIfPseudoIsNull() {
        val pseudo = null
        val response = doRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)

        assertThat(response.readEntity(String::class.java)).isEqualTo(apiErrorMessage)
    }

    @Test
    fun shouldThrowIfPseudoIsEmpty() {
        val pseudo = ""
        val response = doRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)

        assertThat(response.readEntity(String::class.java)).isEqualTo(apiErrorMessage)
    }

    @Test
    fun shouldThrowIfPseudoIsAlreadyRegistered() {
        val pseudo = "competitor"
        doRequest(pseudo)

        val response = doRequest(pseudo)

        assertThat(response.status).isEqualTo(apiErrorStatus)
        assertThat(response.readEntity(ApiError::class.java)).isEqualTo(ApiError(CompetitorPseudoAlreadyUsed(pseudo).message!!))
    }

    private fun doRequest(pseudo: String?): Response {
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