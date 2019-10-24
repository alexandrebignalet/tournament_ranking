package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import tournament_ranking.domain.NameRequiredError
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.exception.ApiError
import tournament_ranking.resources.exception.RestExceptionMapper
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

class CompetitorsResourceTest {

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

        assertThat(response.status).isEqualTo(400)

        val apiError = ApiError(NameRequiredError().message, 400)
        assertThat(response.readEntity(ApiError::class.java)).isEqualTo(apiError)
    }

    @Test
    fun shouldThrowIfPseudoIsEmpty() {
        val pseudo = ""
        val response = doRequest(pseudo)

        assertThat(response.status).isEqualTo(400)

        val apiError = ApiError(NameRequiredError().message, 400)
        assertThat(response.readEntity(ApiError::class.java)).isEqualTo(apiError)
    }

    private fun doRequest(pseudo: String?): Response {
        val command = AddCompetitor(pseudo)
        return resources.target("/tournament/competitors").request().post(Entity.json(command))
    }

    companion object {
        val repository = CompetitorRepository()

        @ClassRule
        @JvmField
        val resources = ResourceTestRule.builder()
            .addResource(CompetitorsResource(repository))
            .addProvider(RestExceptionMapper())
            .build()
    }
}