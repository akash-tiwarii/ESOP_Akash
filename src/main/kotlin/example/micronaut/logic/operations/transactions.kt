package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.model.Transaction
import java.math.BigInteger

fun getTransactionByEsopId(esopId: BigInteger): MutableList<Transaction> {
    if (!esopIdToTransaction.containsKey(esopId)) {
        val errorObject = ErrorMsgs(mutableListOf())
        errorObject.error.add("ESOPId not Found")
        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }

    return esopIdToTransaction.getValue(esopId)

}