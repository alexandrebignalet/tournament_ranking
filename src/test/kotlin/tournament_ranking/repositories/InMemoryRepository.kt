package tournament_ranking.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import tournament_ranking.domain.Entity

class InMemoryRepositoryTest {

    private val repository = InMemoryRepository<AnEntity>()

    @Test
    fun shouldAllowAddingEntities() {
        val entityId = "entity_id"
        val entity = AnEntity(entityId)

        repository.add(entity)

        assertThat(repository.get(entityId)).isEqualTo(entity)
    }

    class AnEntity(id: String): Entity(id)
}