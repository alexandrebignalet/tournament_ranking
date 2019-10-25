package tournament_ranking.resources.exception

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

data class ApiError(val message: String)

class ApiErrorExceptionMapper : ExceptionMapper<ApiErrorException> {

    override fun toResponse(exception: ApiErrorException): Response {
        return Response
            .status(exception.statusCode)
            .entity(ApiError(exception.message!!))
            .build();
    }
}
