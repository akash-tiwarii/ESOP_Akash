package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.model.Transaction
import java.math.BigInteger

fun getTransactionByEsopId(esopId: BigInteger): MutableList<Transaction> {
    if (!esopIdToTransaction.containsKey(esopId)) {
        val errorObject = Error(mutableListOf())
        errorObject.messages.add("ESOPId not Found")
        throw ApplicationException(errorObject.messages.joinToString(separator = ","))
    }

    return esopIdToTransaction.getValue(esopId)

}