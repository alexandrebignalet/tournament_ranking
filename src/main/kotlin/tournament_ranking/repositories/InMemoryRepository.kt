package tournament_ranking.repositories

import tournament_ranking.domain.Entity

open class InMemoryRepository<EType: Entity> {

    private val byId = HashMap<String, EType>()

    fun get(entityId: String): EType? {
        return byId[entityId]
    }

    fun add(entity: EType) {
        byId[entity.id()] = entity
    }

}