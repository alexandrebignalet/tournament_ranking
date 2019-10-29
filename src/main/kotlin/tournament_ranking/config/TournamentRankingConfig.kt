package tournament_ranking.config

import io.dropwizard.Configuration

class TournamentRankingConfig(
    val name: String = "Tournament Ranking Test",
    val mode: String = "dynamodb",
    val endpointURL: String = "http://localhost:4569",
    val accessKey: String = "accessKey",
    val accessSecret: String = "accessSecret",
    val competitorsTableName: String = "competitors-test"
) : Configuration()