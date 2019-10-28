package tournament_ranking.repositories

import tournament_ranking.domain.Entity

open class InMemoryRepository<TEntity: Entity> {

    protected var byId: MutableMap<String, TEntity> = HashMap()

    open fun get(entityId: String): TEntity? {
        return byId[entityId]
    }

    open fun add(entity: TEntity) {
        byId[entity.id()] = entity
    }
}