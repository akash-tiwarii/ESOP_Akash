package example.micronaut.controller

import com.fasterxml.jackson.core.JsonParseException
import example.micronaut.errors.ErrorMsgs
import example.micronaut.logic.operations.*
import example.micronaut.model.*
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.hateoas.JsonError
import java.math.BigInteger
import java.util.*

var noOfOrders = 0

@Controller("/user")
class ESOPController {
    @Post("/register")
    fun registerUserCaller(@Body reg: Register): MutableHttpResponse<out Any>? //Register // @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    {
        reg.email = reg.email.lowercase()
        val response = registerUser(reg)
        return if (response is Message) {
            HttpResponse.created(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/{userName}/order")
    fun placeOrderCaller(@Body ord: Order, @PathVariable userName: String): Any    //AccountInfo
    {
        ord.type = ord.type.uppercase()
        ord.esopType = ord.esopType.uppercase()
        val response = placeOrder(ord, userName)
        return if (response is OrderResponse) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Get("/{userName}/accountInformation")
    fun getAccountInfoCaller(@PathVariable userName: String): Any  //AccountInfo
    {

        val response = getAccountInfo(userName)
        return if (response is AccountInfo) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/{userName}/inventory")
    fun validateInventoryCaller(
        @Body inventoryObject: AddInventory,
        @PathVariable userName: String
    ): Any       //AddInventory
    {
        inventoryObject.type = inventoryObject.type.uppercase()
        val response = validateInventory(inventoryObject, userName)
        return if (response is Message) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/{userName}/wallet")
    fun validateWalletCaller(@Body walletObject: AddWallet, @PathVariable userName: String): Any {
        val response = validateWallet(walletObject, userName)
        return if (response is Message) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Get("/{userName}/orderHistory")
    fun historyOperationsCaller(@PathVariable userName: String): Any {
        val response = getHistoryOf(userName)
        return if (response is ErrorMsgs) {
            HttpResponse.badRequest(response)
        } else {
            HttpResponse.ok(response)
        }
    }

    @Post("/{userName}/esops")
    fun allEsopsHeldByUser(@PathVariable userName: String): MutableMap<String, MutableList<BigInteger>> {
        val response = getEsops(userName)
//        return if (response is Message) {
//            HttpResponse.ok(response)
//        } else {
//            HttpResponse.badRequest(response)
//        }
        return response
    }

    @Error
    fun jsonError(request: HttpRequest<*>, e: JsonParseException): HttpResponse<String> {
        val error = "{\"errors\":[\"Invalid JSON format\"]}"

        return HttpResponse.status<JsonError>(HttpStatus.BAD_REQUEST, "Fix Your JSON")
            .body(error)
    }

}