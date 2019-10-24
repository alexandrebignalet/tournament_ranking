package tournament_ranking.resources

import io.dropwizard.testing.junit.ResourceTestRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import javax.ws.rs.core.Response

class DefaultResourceTest {

    @Test
    fun itWorks() {
        val response = resources.target("/").request().get(Response::class.java)
        assertThat(response.status).isEqualTo(200)
        assertThat(response.readEntity(String::class.java)).isEqualTo("hello")
    }

    companion object {
        @ClassRule
        @JvmField
        val resources = ResourceTestRule.builder()
            .addResource(DefaultResource())
            .build()
    }
}