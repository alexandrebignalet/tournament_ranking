package tournament_ranking.domain

interface CompetitorRepository {

    fun get(competitorId: String): Competitor?

    fun add(competitor: Competitor)

    fun getCompetitorRank(entityId: String): Pair<Int, Competitor?>

    fun rankList(): List<Competitor>

    fun reset()
}