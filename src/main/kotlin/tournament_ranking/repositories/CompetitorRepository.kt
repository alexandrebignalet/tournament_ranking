package tournament_ranking.repositories

import tournament_ranking.domain.Competitor

public class CompetitorRepository: InMemoryRepository<Competitor>() {
    fun rankList(): List<Competitor> {
       return byId.values.sortedByDescending { it.points  }
    }
}