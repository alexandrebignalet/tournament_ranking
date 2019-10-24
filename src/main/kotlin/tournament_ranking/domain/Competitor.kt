package tournament_ranking.domain

public class Competitor(pseudo: String?) : Entity {
    private val pseudo: String

    init {
        if (pseudo.isNullOrEmpty()) throw NameRequiredError()
        this.pseudo = pseudo
    }

    override fun id(): String {
        return pseudo
    }
}