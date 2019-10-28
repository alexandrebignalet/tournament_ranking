package tournament_ranking.resources

import tournament_ranking.domain.Competitor
import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.dto.UpdateCompetitorPoints
import tournament_ranking.resources.exception.CompetitorNotFound
import tournament_ranking.resources.exception.CompetitorPseudoAlreadyUsed
import javax.validation.Valid
import javax.ws.rs.*
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

    @PUT
    @Path("/{pseudo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateCompetitorPoints(@PathParam("pseudo") pseudo: String, @Valid command: UpdateCompetitorPoints): Response {

        val competitor = repository.get(pseudo) ?: throw CompetitorNotFound(pseudo)

        competitor.points = command.points

        repository.add(competitor)

        return Response
            .status(Response.Status.NO_CONTENT)
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun rankList(): Response {
        val rankList = repository.rankList()

        return Response
            .ok(rankList)
            .build();
    }

    @GET
    @Path("/{competitorId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCompetitorWithRank(@PathParam("competitorId") competitorId: String): Response {
        val competitor = repository.get(competitorId)

        return Response
            .ok(competitor)
            .build();
    }
}
