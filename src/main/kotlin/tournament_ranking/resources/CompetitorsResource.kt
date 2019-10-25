package tournament_ranking.resources

import tournament_ranking.domain.Competitor
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.exception.CompetitorPseudoAlreadyUsed
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/tournament/competitors")
class CompetitorsResource(private val repository: CompetitorRepository) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun addCompetitor(@Valid command: AddCompetitor): Response {

        val pseudo = command.pseudo!!

        if (repository.get(pseudo) != null) throw CompetitorPseudoAlreadyUsed(pseudo)

        val competitor = Competitor(pseudo)

        repository.add(competitor)

        return Response
            .status(Response.Status.CREATED)
            .build();
    }
}
