package example.micronaut.controller

import com.fasterxml.jackson.core.JsonParseException
import example.micronaut.logic.operations.*
import example.micronaut.model.*
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.hateoas.JsonError
import java.math.BigInteger
import java.util.*

var noOfOrders = 0

@Controller
class ESOPController {
    @Post("/user/register")
    fun registerUserCaller(@Body reg: Register): HttpResponse<Message>//Register // @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    {
        reg.email = reg.email.lowercase()
        val response = registerUser(reg)
        return HttpResponse.created(response)
    }

    @Post("/user/{userName}/order")
    fun placeOrderCaller(@Body ord: Order, @PathVariable userName: String): HttpResponse<*>     //AccountInfo
    {
        ord.type = ord.type.uppercase()
        ord.esopType = ord.esopType.uppercase()
        val response = placeOrder(ord, userName)
        return HttpResponse.ok(response)
    }

    @Get("/user/{userName}/accountInformation")
    fun getAccountInfoCaller(@PathVariable userName: String): HttpResponse<*>   //AccountInfo
    {

        val response = getAccountInfo(userName)
        return HttpResponse.ok(response)
    }

    @Post("/user/{userName}/inventory")
    fun validateInventoryCaller(
        @Body inventoryObject: AddInventory,
        @PathVariable userName: String
    ): HttpResponse<*>        //AddInventory
    {
        inventoryObject.type = inventoryObject.type.uppercase()
        val response = validateInventory(inventoryObject, userName)
        return HttpResponse.created(response)
    }

    @Post("/user/{userName}/wallet")
    fun validateWalletCaller(@Body walletObject: AddWallet, @PathVariable userName: String): HttpResponse<*> {
        val response = validateWallet(walletObject, userName)
        return HttpResponse.ok(response)
    }

    @Get("/user/{userName}/orderHistory")
    fun historyOperationsCaller(@PathVariable userName: String): HttpResponse<*> {
        val response = getHistoryOf(userName)
        return HttpResponse.ok(response)
    }

    @Get("/user/{userName}/esops")
    fun allEsopsHeldByUser(@PathVariable userName: String): HttpResponse<*> {
        val response = getEsops(userName)
        return HttpResponse.ok(response)
    }

    @Get("/esopID/{esopId}")
    fun allEsopsHeldByUser(@PathVariable esopId: BigInteger): HttpResponse<*> {
        val response = getTransactionByEsopId(esopId)
        return HttpResponse.ok(response)
    }

    @Get("/organisationInfo")
    fun totalTransactionFee(): Any {
        return "Total Transaction Fee Collected : " + getTransactionFeeToOrganization()
    }

    @Error
    fun jsonError(request: HttpRequest<*>, e: JsonParseException): HttpResponse<String> {
        val error = "{\"errors\":[\"Invalid JSON format\"]}"

        return HttpResponse.status<JsonError>(HttpStatus.BAD_REQUEST, "Fix Your JSON")
            .body(error)
    }

}