package example.micronaut.controller

import com.fasterxml.jackson.core.JsonParseException
import example.micronaut.logic.operations.*
import example.micronaut.model.*
import example.micronaut.repository.OrderRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.hateoas.JsonError
import io.micronaut.validation.Validated
import java.math.BigInteger
import java.util.*
import javax.validation.Valid

var noOfOrders = 0
var totalTransactionFee = 0.toBigInteger()
var totalTaxCollected = 0.toBigInteger()

@Validated
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
        ord.type = ord.type
        ord.esopType = ord.esopType
        val response = placeOrder(ord, userName)
        return HttpResponse.created(response)
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
        inventoryObject.type = inventoryObject.type
        val response = validateInventory(inventoryObject, userName)
        return HttpResponse.created(response)
    }

    @Post("/user/{userName}/wallet")
    fun validateWalletCaller(@Body walletObject: AddWallet, @PathVariable userName: String): HttpResponse<*> {
        val response = validateWallet(walletObject, userName)
        return HttpResponse.created(response)
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
        return "Total Transaction Fee Collected : $totalTransactionFee"
    }

    @Get("/taxCollected")
    fun totalTaxCollected(): Any {
        return "Total Transaction Fee Collected : $totalTaxCollected"
    }

    @Delete("/user/{userName}/order/{orderId}")
    fun cancelOrderForGivenId(
        @Body @Valid cancelRequest: OrderCancel,
        @PathVariable userName: String,
        @PathVariable orderId: Int
    ): HttpResponse<*> {
        val orderRepository = OrderRepository()
        val response = orderRepository.cancelOrder(cancelRequest, userName, orderId)
        return HttpResponse.ok(response)
    }

    @Error
    fun jsonError(request: HttpRequest<*>, e: JsonParseException): HttpResponse<String> {
        val error = "{\"errors\":[\"Invalid JSON format\"]}"
        return HttpResponse.status<JsonError>(HttpStatus.BAD_REQUEST, "Fix Your JSON")
            .body(error)
    }

}
