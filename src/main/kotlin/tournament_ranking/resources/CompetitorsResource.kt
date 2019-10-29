package tournament_ranking.resources

import tournament_ranking.domain.Competitor
import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.query.GetCompetitorWithRank
import tournament_ranking.resources.dto.AddCompetitor
import tournament_ranking.resources.dto.ChangeCompetitorPoints
import tournament_ranking.resources.exception.ApiError
import tournament_ranking.resources.exception.CompetitorNotFound
import tournament_ranking.resources.exception.CompetitorPseudoAlreadyUsed
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/tournament/competitors")
@Produces(MediaType.APPLICATION_JSON)
class CompetitorsResource @Inject constructor(private val repository: CompetitorRepository) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addCompetitor(@Valid command: AddCompetitor): Response {

        val pseudo = command.pseudo!!

        if (repository.exists(pseudo)) throw CompetitorPseudoAlreadyUsed(pseudo)

        val competitor = Competitor(pseudo)

        repository.save(competitor)

        return Response
            .status(Response.Status.CREATED)
            .build();
    }

    @PUT
    @Path("/{pseudo}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateCompetitorPoints(@PathParam("pseudo") pseudo: String, @Valid command: ChangeCompetitorPoints): Response {

        val competitor = repository.get(pseudo) ?: throw CompetitorNotFound(pseudo)

        competitor.points += command.points

        repository.save(competitor)

        return Response
            .status(Response.Status.NO_CONTENT)
            .build();
    }

    @GET
    fun rankList(): Response {
        val rankList = repository.rankList()

        return Response
            .ok(rankList)
            .build();
    }

    @GET
    @Path("/{competitorId}")
    fun getCompetitorWithRank(@PathParam("competitorId") competitorId: String): Response {
        val query = GetCompetitorWithRank(repository)

        val competitor = query.run(competitorId) ?: throw CompetitorNotFound(competitorId)

        return Response.ok(competitor).build()
    }

    @DELETE
    fun resetTournamentCompetitors(): Response {
        repository.reset()

        return Response.noContent().build();
    }
}
