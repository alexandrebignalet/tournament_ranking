package tournament_ranking.repositories

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import tournament_ranking.domain.Competitor
import tournament_ranking.domain.Entity
import kotlin.concurrent.fixedRateTimer

class CompetitorRepositoryTest {

    private val repository = CompetitorRepository()

    @Test
    fun shouldUpdateCompetitorRankAfterAdd() {
        val pseudo = "first"

        repository.add(Competitor(pseudo))

        val addedCompetitor = repository.get(pseudo)

        assertThat(addedCompetitor?.rank).isEqualTo(1)
    }

    @Test
    fun shouldUpdateAllCompetitorsRankAfterAdd() {
        val firstPseudo = "first"
        val secondPseudo = "second"
        val thirdPseudo = "third"

        repository.add(Competitor(thirdPseudo, 10))
        repository.add(Competitor(secondPseudo, 20))
        repository.add(Competitor(firstPseudo, 30))

        val first = repository.get(firstPseudo)
        val second = repository.get(secondPseudo)
        val third = repository.get(thirdPseudo)

        assertThat(first?.rank).isEqualTo(1)
        assertThat(second?.rank).isEqualTo(2)
        assertThat(third?.rank).isEqualTo(3)
    }
}