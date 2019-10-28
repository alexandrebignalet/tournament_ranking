package tournament_ranking.repositories

import tournament_ranking.domain.Entity

open class InMemoryRepository<TEntity: Entity> {

    protected val byId = HashMap<String, TEntity>()

    fun get(entityId: String): TEntity? {
        return byId[entityId]
    }

    fun add(entity: TEntity) {
        byId[entity.id()] = entity
    }
}