package tournament_ranking.resources.exception

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiError
    @JsonCreator
    constructor(@JsonProperty("message") val message: String?, @JsonProperty("statusCode") val statusCode: Int)