package tournament_ranking.config

import io.dropwizard.Configuration

class DynamoDBConfig(val endpointURL: String = "endpointURL",
                     val accessKey: String = "accessKey",
                     val accessSecret: String = "accessSecret") : Configuration() {
    companion object {
        const val competitorTableName = "competitors"
    }
}