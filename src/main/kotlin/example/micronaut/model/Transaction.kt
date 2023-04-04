package example.micronaut.model

import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(
    val buyerId: String,
    val sellerId: String,
    val transactionType: TransactionType,
    val cost: String,
//    val timestamp: String,
) {
    companion object {
        private var currentId: BigInteger = 1.toBigInteger()
    }

    val transactionId: BigInteger = currentId++
    val timestamp: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return LocalDateTime.now().format(formatter)
        }
}