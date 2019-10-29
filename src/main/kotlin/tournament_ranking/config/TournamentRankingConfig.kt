package tournament_ranking.config

import io.dropwizard.Configuration

class TournamentRankingConfig(
    val name: String = "default",
    val mode: String = "in-memory",
    val endpointURL: String = "in-memory",
    val accessKey: String = "in-memory",
    val accessSecret: String = "in-memory"
) : Configuration() {

    fun dynamoDbConfig(): DynamoDBConfig {
        return DynamoDBConfig(endpointURL, accessKey, accessSecret)
    }
}