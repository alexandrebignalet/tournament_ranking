package tournament_ranking

import io.dropwizard.Configuration

class TournamentRankingConfig(val name: String = "default", val mode: String = "in-memory") : Configuration()