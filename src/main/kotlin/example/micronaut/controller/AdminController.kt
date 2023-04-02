package example.micronaut.controller

import com.fasterxml.jackson.core.JsonParseException
import example.micronaut.logic.operations.getTransactionByEsopId
import example.micronaut.logic.operations.getTransactionFeeToOrganization
import example.micronaut.model.Transaction
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.hateoas.JsonError
import java.math.BigInteger

@Controller("/admin")
class AdminController {

    @Get("/org")
    fun totalTransactionFee(): Any {
        return "Total Transaction Fee Collected : " + getTransactionFeeToOrganization()

    }

    @Get("/esop/{esopId}")
    fun transactionByEsopId(@PathVariable esopId: BigInteger): MutableList<Transaction> {

//
//        return if (response is Message) {
//            HttpResponse.ok(response)
//        } else {
//            HttpResponse.badRequest(response)
//        }
        return getTransactionByEsopId(esopId)
    }


    @Error
    fun jsonError(request: HttpRequest<*>, e: JsonParseException): HttpResponse<String> {
        val error = "{\"errors\":[\"Invalid JSON format\"]}"

        return HttpResponse.status<JsonError>(HttpStatus.BAD_REQUEST, "Fix Your JSON")
            .body(error)
    }
}