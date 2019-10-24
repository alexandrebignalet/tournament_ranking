package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.CreateCompetitor
import javax.ws.rs.client.Entity

class CompetitorsResourceTest {

    @Test
    fun shouldAddACompetitorWithHisPseudo() {
        val pseudo = "Jack"
        val command = CreateCompetitor(pseudo)
        val response = resources.target("/tournament/competitors").request().post(Entity.json(command))

        assertThat(response.status).isEqualTo(201)

        val createdCompetitor = repository.get(pseudo)
        assertThat(createdCompetitor).isNotNull
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