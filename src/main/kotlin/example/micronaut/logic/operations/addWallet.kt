package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkUserPresence
import example.micronaut.model.AddWallet
import example.micronaut.model.Message
import java.math.BigInteger

fun validateWallet(walletObject: AddWallet, userName: String): Message {
    val errorObject = Error(mutableListOf())
    if (!checkUserPresence(userName)) {
        errorObject.messages.add("User not registered")
        throw ApplicationException(errorObject.messages.joinToString(separator = ","))
    }
    val amt: BigInteger
    try {
        amt = walletObject.amount.toBigInteger()
    } catch (e: Exception) {
        errorObject.messages.add("Invalid field name or value for the field 'amount' ")
        errorObject.messages.add("Amount should be a positive Integer not exceeding 9223372036854775806")
        throw ApplicationException(errorObject.messages.joinToString(separator = ","))
    }
    if (!(amt > BigInteger("0") && amt <= BigInteger("9223372036854775806"))) {
        errorObject.messages.add("Amount should be a positive Integer not exceeding 9223372036854775806")
        throw ApplicationException(errorObject.messages.joinToString(separator = ","))
    }

    addWallet(userName, amt)

    return Message("$amt added to account")
}

fun addWallet(userName: String, amt: BigInteger) {
    for (user in usersArray) {
        if (user.userName == userName) user.wallet.free += amt
    }
}