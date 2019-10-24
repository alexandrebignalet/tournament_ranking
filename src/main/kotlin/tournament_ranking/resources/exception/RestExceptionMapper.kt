package tournament_ranking.resources.exception

import tournament_ranking.domain.DomainError
import java.lang.Error
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class RestExceptionMapper: ExceptionMapper<DomainError> {

    override fun toResponse(exception: DomainError): Response {
        val statusCode = 400

        return Response.status(statusCode)
            .entity(ApiError(exception.message, statusCode))
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}