package example.micronaut.logic.operations

import example.micronaut.model.Transaction
import java.math.BigInteger

fun getTransactionByEsopId (esopId: BigInteger) : MutableList<Transaction> {
    return esopIdToTransaction.getValue(esopId)

}