package tournament_ranking.repositories

import tournament_ranking.domain.Competitor
import tournament_ranking.domain.CompetitorRepository

class CompetitorInMemoryRepository: CompetitorRepository {

    private val inMemoryRepository = InMemoryRepository<Competitor>()

    override fun exists(competitorId: String): Boolean {
        return get(competitorId) != null;
    }

    override fun get(competitorId: String): Competitor? {
        return inMemoryRepository.get(competitorId)
    }

    override fun save(competitor: Competitor) {
        return inMemoryRepository.add(competitor)
    }

    override fun getCompetitorRank(entityId: String): Pair<Int, Competitor?> {
        val rank = rankList().map(Competitor::pseudo).indexOf(entityId) + 1
        return rank to get(entityId)
    }

    override fun rankList(): List<Competitor> {
        return inMemoryRepository
            .values()
            .sortedByDescending { it.points  }
    }

    override fun reset() {
        return inMemoryRepository.reset()
    }
}