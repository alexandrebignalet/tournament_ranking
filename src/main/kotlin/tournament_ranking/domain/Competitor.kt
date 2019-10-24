package tournament_ranking.domain

data class Competitor(val pseudo: String): Entity {

    override fun id(): String {
        return pseudo
    }
}