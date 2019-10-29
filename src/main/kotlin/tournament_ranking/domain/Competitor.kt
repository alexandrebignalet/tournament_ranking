package tournament_ranking.domain

data class Competitor(val pseudo: String, var points: Int = 0) : Entity {

    override fun id() = pseudo
}