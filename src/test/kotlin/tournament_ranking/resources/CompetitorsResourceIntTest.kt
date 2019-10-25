package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.AddCompetitor
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

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
            .build()
    }
}