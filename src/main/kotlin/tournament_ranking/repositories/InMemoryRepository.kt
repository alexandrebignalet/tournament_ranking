package tournament_ranking.repositories

import com.google.common.collect.ImmutableList
import tournament_ranking.domain.Entity

class InMemoryRepository<EType: Entity> {

    private val byId = HashMap<String, EType>()

    fun get(entityId: String): EType? {
        return byId[entityId]
    }

    fun add(entity: EType) {
        byId[entity.id] = entity
    }

}