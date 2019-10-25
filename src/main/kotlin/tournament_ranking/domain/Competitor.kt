package tournament_ranking.domain

public class Competitor(private val pseudo: String) : Entity {

    override fun id(): String {
        return pseudo
    }
}