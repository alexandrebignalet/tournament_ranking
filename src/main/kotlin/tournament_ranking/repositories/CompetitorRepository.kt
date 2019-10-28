package tournament_ranking.repositories

import tournament_ranking.domain.Competitor

class CompetitorRepository: InMemoryRepository<Competitor>() {

    override fun add(entity: Competitor) {
        super.add(entity)

        updateRanking()
    }

    fun rankList(): List<Competitor> {
        return byId.values.sortedByDescending { it.points  }
    }

    private fun updateRanking() {
        byId = rankList()
            .mapIndexed { idx, it -> Competitor(it.pseudo, it.points, idx + 1) }
            .associateBy( { it.pseudo }, { it })
            .toMutableMap()
    }
}