package tournament_ranking.domain

data class Competitor(val pseudo: String, var points: Int = 0, var rank: Int? = null) : Entity {

    override fun id() = pseudo
}