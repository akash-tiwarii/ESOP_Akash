package example.micronaut.model

import java.math.BigInteger
import java.sql.Timestamp

data class Transaction(
    val buyerId: String,
    val sellerId: String,
    val transactionType: String,
    val cost: String,
    val timestamp: String,
){
    companion object {
        private var currentId : BigInteger = 1.toBigInteger()
    }
    val transactionId : BigInteger = currentId++
}