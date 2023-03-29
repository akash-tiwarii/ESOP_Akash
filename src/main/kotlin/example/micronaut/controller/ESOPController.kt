package com.example.controller

import com.example.errors.ErrorMsgs
import com.example.logic.*
import com.example.logic.checks.*
import com.example.logic.operations.getHistoryOf
import com.example.logic.operations.placeOrder
import com.example.logic.operations.registerUser
import com.example.model.*
import com.example.logic.operations.getAccountInfo
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import com.example.logic.operations.validateInventory
import com.example.logic.operations.validateWallet
import example.micronaut.logic.operations.getTransactionFeeToOrganization
import java.util.*

var noOfOrders=0
@Controller
class ESOPController {
    @Post("/user/register")
    fun registerUserCaller(@Body reg: Register): MutableHttpResponse<out Any>? //Register // @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    {
        reg.email=reg.email.lowercase()
       val response = registerUser(reg)
        return if(response is Message) {
            HttpResponse.created(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/user/{userName}/order")
    fun placeOrderCaller(@Body ord:Order,@PathVariable userName:String):Any    //AccountInfo
    {
        ord.type = ord.type.uppercase()
        ord.esopType = ord.esopType.uppercase()
        val response= placeOrder(ord,userName)
        return if(response is OrderResponse) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }
    @Get("/user/{userName}/accountInformation")
    fun getAccountInfoCaller(@PathVariable userName:String):Any  //AccountInfo
    {

        val response= getAccountInfo(userName)
        return if(response is AccountInfo) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/user/{userName}/inventory")
    fun validateInventoryCaller(@Body inventoryObject:AddInventory,@PathVariable userName: String):Any       //AddInventory
    {
        inventoryObject.type = inventoryObject.type.uppercase()
        val response= validateInventory(inventoryObject,userName)
        return if(response is Message) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Post("/user/{userName}/wallet")
    fun validateWalletCaller(@Body walletObject:AddWallet,@PathVariable userName: String):Any
    {
        val response= validateWallet(walletObject,userName)
        return if(response is Message) {
            HttpResponse.ok(response)
        } else {
            HttpResponse.badRequest(response)
        }
    }

    @Get("/user/{userName}/orderHistory")
    fun historyOperationsCaller(@PathVariable userName: String):Any
    {
        val response= getHistoryOf(userName)
        return if(response is ErrorMsgs) {
            HttpResponse.badRequest(response)
        } else {
            HttpResponse.ok(response)
        }
    }

    @Get("/admin/org")
    fun totalTransactionFee(): Any {
        //        return if(response is ErrorMsgs) {
//            HttpResponse.badRequest(response)
//        } else {
//            HttpResponse.ok(response)
//        }
        return "Total Transaction Fee Collected : " + getTransactionFeeToOrganization()

    }

}