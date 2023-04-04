package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.logic.checks.checkUserPresence

fun updateOrDeleteQuantity(username: String, orderId: String) {
    val errorObject = Error(mutableListOf())
    if (!checkUserPresence(username)) {
        errorObject.messages.add("User not registered")
//        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }
//    if(!checkOrderPresence(orderId)){
//
//    }
}