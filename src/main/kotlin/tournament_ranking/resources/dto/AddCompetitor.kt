package tournament_ranking.resources.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty

data class AddCompetitor
    @JsonCreator
    constructor(
        @JsonProperty("pseudo")
        @field:NotEmpty(message = "est obligatoire")
        val pseudo: String?
    )