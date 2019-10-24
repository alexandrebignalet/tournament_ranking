package tournament_ranking.resources.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AddCompetitor
    @JsonCreator
    constructor(@JsonProperty("pseudo") val pseudo: String?)