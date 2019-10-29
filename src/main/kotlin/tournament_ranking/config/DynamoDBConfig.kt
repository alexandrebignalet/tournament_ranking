package tournament_ranking.config

import io.dropwizard.Configuration

class DynamoDBConfig(val endpointURL: String = "http://localhost:4569",
                     val accessKey: String = "accessKey",
                     val accessSecret: String = "accessSecret",
                     val competitorsTableName: String = "competitors-test") : Configuration() {
}