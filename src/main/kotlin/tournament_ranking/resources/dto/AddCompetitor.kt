package tournament_ranking.resources.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateCompetitor
    @JsonCreator
    constructor(@JsonProperty("pseudo") var pseudo: String)