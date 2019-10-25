package tournament_ranking.resources.exception

open class ApiErrorException(message: String, val statusCode: Int = 422) : Throwable(message)
