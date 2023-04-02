package example.micronaut.model

import java.sql.Timestamp

data class Transaction(
    val transactionId: String,
    val buyerId: String,
    val sellerId: String,
    val transactionType: String,
    val cost: String,
    val timestamp: Timestamp,
)