package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkOrderPresence
import example.micronaut.logic.checks.checkUserPresence

fun updateOrDeleteQuantity(username: String,orderId: String) {
    val errorObject = ErrorMsgs(mutableListOf())
    if(!checkUserPresence(username)){
        errorObject.error.add("User not registered")
//        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }
    if(!checkOrderPresence(orderId)){

    }
}