package tournament_ranking.resources.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateCompetitorPoints
    @JsonCreator
    constructor(
        @JsonProperty("points") val points: Int
    )
