package tournament_ranking.repositories

import tournament_ranking.domain.Competitor

class CompetitorRepository: InMemoryRepository<Competitor>() {

    fun getCompetitorRank(entityId: String): Pair<Int, Competitor?> {
        val rank = rankList().map(Competitor::pseudo).indexOf(entityId) + 1
        return rank to get(entityId)
    }

    fun rankList(): List<Competitor> {
        return byId
            .values
            .sortedByDescending { it.points  }
    }
}