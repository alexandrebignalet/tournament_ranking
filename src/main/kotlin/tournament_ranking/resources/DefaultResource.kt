package tournament_ranking.resources

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/")
class DefaultResource {

    @GET
    fun hello(): String {
        return "hello"
    }
}