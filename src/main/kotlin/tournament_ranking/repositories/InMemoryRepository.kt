package tournament_ranking.repositories

import tournament_ranking.domain.Competitor
import tournament_ranking.domain.Entity

open class InMemoryRepository<TEntity: Entity> {

    protected var byId = HashMap<String, TEntity>()

    open fun get(entityId: String): TEntity? {
        return byId[entityId]
    }

    open fun add(entity: TEntity) {
        byId[entity.id()] = entity
    }

    fun values(): MutableCollection<TEntity> {
        return byId.values
    }

    fun reset() {
        byId = HashMap()
    }
}