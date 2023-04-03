package example.micronaut.model

data class UserEsops(
    val esopId: String,
    val userId: String,
    val transactionType: String,
    val cost: String,
    val timeStamp: String,
    val transactionId: String
)
