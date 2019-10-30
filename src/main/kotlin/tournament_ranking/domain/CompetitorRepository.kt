package tournament_ranking.domain

interface CompetitorRepository {

    fun get(competitorId: String): Competitor?

    fun save(competitor: Competitor)

    fun getCompetitorRank(entityId: String): Pair<Int, Competitor?> {
        val rank = rankList().map(Competitor::pseudo).indexOf(entityId) + 1
        return rank to get(entityId)
    }

    fun rankList(): List<Competitor>

    fun reset()

    fun exists(competitorId: String): Boolean
}