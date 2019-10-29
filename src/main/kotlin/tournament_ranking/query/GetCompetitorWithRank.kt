package tournament_ranking.query

import tournament_ranking.domain.CompetitorRepository
import tournament_ranking.resources.dto.CompetitorWithRank

class GetCompetitorWithRank(private val repository: CompetitorRepository) {

    fun run(competitorId: String): CompetitorWithRank? {
        val (rank, competitor) = repository.getCompetitorRank(competitorId)
        return competitor?.let {
            return CompetitorWithRank(competitor.pseudo, competitor.points, rank)
        }
    }
}