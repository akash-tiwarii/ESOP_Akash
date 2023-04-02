package example.micronaut.logic.checks

import example.micronaut.errors.ErrorMsgs
import example.micronaut.logic.operations.addEsopsFromFreeToLocked
import example.micronaut.logic.operations.usersArray
import example.micronaut.model.Order
import java.math.BigInteger

fun checkOrder(orderObject: Order, uname: String): Any {

    val errorObject = ErrorMsgs(mutableListOf())

    var price = BigInteger("0")
    var qnt = BigInteger("0")


    if (orderObject.type == "BUY" && orderObject.esopType != "") {
        errorObject.error.add("BUY order cannot have a performance field")
    }
    if (orderObject.type == "SELL" && (orderObject.esopType != "PERFORMANCE" && orderObject.esopType != "NORMAL")) {
        errorObject.error.add("Invalid field name or value for 'esopType' field")
        errorObject.error.add("Add a valid input for 'esopType' field")
    }
    try {
        qnt = orderObject.quantity.toBigInteger()
        if (qnt <= BigInteger("0"))
            throw Exception("")
    } catch (e: Exception) {
        errorObject.error.add("Invalid field name or value for the field 'quantity'")
        errorObject.error.add("Quantity must be positive integers")
    }
    try {
        price = orderObject.price.toBigInteger()
        if (price <= BigInteger("0"))
            throw Exception("")
    } catch (e: Exception) {
        errorObject.error.add("Invalid field name or value for the field 'price'")
        errorObject.error.add("Price must be positive integers")
    }
    if (errorObject.error.size != 0) {
        return errorObject
    }

    var userFound = false

    for (user in usersArray) {
        if (uname == user.userName) {
            userFound = true
            if (price <= BigInteger("0")) {
                errorObject.error.add("Price must be positive integer")
            }
            if (qnt <= BigInteger("0")) {
                errorObject.error.add(" Quantity must be positive integer")
            }
            if (orderObject.type == "BUY") {
                if (price > BigInteger("0") && qnt > BigInteger("0") && qnt * price <= user.wallet.free) {
                    user.wallet.free -= qnt * price
                    user.wallet.locked += qnt * price
                    return true
                } else {
                    errorObject.error.add("Insufficient balance")
                }
            } else if (orderObject.type == "SELL") {
                // PERFORMANCE_type==NORMAL
                if (orderObject.esopType == "NORMAL") {
                    if (qnt > user.inventory[0].free)
                        errorObject.error.add("Insufficient ESOPs in inventory")

                    if (price > BigInteger("0") && qnt > BigInteger("0") && qnt <= user.inventory[0].free) {
                        user.inventory[0].free -= qnt
                        addEsopsFromFreeToLocked(user,qnt,0)
                        user.inventory[0].locked += qnt

                        return true
                    }
                }
                // PERFORMANCE_type==PERFORMANCE
                else {
                    if (qnt > user.inventory[1].free)
                        errorObject.error.add("Insufficient ESOPs in inventory")

                    if (price > BigInteger("0") && qnt > BigInteger("0") && qnt <= user.inventory[1].free) {
                        user.inventory[1].free -= qnt
                        addEsopsFromFreeToLocked(user,qnt,1)
                        user.inventory[1].locked += qnt
                        return true
                    }

                }
            } else {
                errorObject.error.add("Type must be either BUY or SELL only")
            }
        }

    }

    if (!userFound) {
        errorObject.error.clear()
        errorObject.error.add("User not registered")

    }
    return errorObject

}