package tournament_ranking.query

import tournament_ranking.repositories.CompetitorRepository
import tournament_ranking.resources.dto.CompetitorWithRank

class GetCompetitorWithRank(val repository: CompetitorRepository) {

    fun run(competitorId: String): CompetitorWithRank? {
        val (rank, competitor) = repository.getCompetitorRank(competitorId)
        return competitor?.let {
            return CompetitorWithRank(competitor.pseudo, competitor.points, rank)
        }
    }
}