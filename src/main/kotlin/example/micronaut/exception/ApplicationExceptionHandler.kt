package example.micronaut.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [ApplicationException::class, ExceptionHandler::class])
class ApplicationExceptionHandler : ExceptionHandler<ApplicationException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>?, exception: ApplicationException): HttpResponse<*> {
        return HttpResponse.badRequest(mapOf("Error" to exception.message!!.split(",")))
    }
}