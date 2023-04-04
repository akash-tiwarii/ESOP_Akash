package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkUserPresence
import example.micronaut.model.*
import java.math.BigInteger

fun validateInventory(inventoryObject: AddInventory, userName: String): Message {
    val errorObject = ErrorMsgs(mutableListOf())
    if (!checkUserPresence(userName)) {
        errorObject.error.add("User not registered")
        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }
    val quantity: BigInteger
    val type = inventoryObject.type

    try {
        quantity = inventoryObject.quantity.toBigInteger()
    } catch (e: Exception) {
        errorObject.error.add("Invalid field name or value for the field 'quantity'")
        errorObject.error.add("Inventory should be a positive Integer not exceeding 9223372036854775806")
        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }

    if (!(quantity > BigInteger.ZERO && quantity <= BigInteger("9223372036854775806"))) {
        errorObject.error.add("Inventory should be a positive Integer not exceeding 9223372036854775806")
    }

//    if (type == EsopType.NORMAL && type == EsopType.PERFORMANCE){
//        errorObject.error.add("Invalid value or field name for 'type' ")
//    }

    if (errorObject.error.size > 0)
        throw ApplicationException(errorObject.error.joinToString(separator = ","))

    addInventory(userName, type, quantity)

    return Message("$quantity ${inventoryObject.type} ESOPs added to account")

}


fun addInventory(userName: String, type: EsopType, quantity: BigInteger) {
    for (user in usersArray) {
        if (user.userName == userName) {
            if (type == EsopType.NORMAL) {
                addingToInventory(user, quantity, 0)
            } else if (type == EsopType.PERFORMANCE) {
                addingToInventory(user, quantity, 1)
            }
            break
        }
    }
}

fun addingToInventory(user: AccountInfo, quantity: BigInteger, index: Int) {
    user.inventory[index].free += quantity
    for (i in 1..quantity.toInt()) {
        val esop = Esop(mutableListOf())
        esopIdToTransaction[esop.esopId] = mutableListOf()
        esopIdToTransaction[esop.esopId]?.add(
            Transaction(
                user.userName,
                "organisation",
                TransactionType.PERFROMANCE,
                "0"
            )
        )
        user.inventory[index].esopsFree.add(esop.esopId)
    }
}
