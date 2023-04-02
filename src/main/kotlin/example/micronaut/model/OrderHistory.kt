package example.micronaut.model

data class Status(
    var price: Int,
    var quantity: Int
)


data class OrderHistory(
    var orderId: String,
    var quantity: Int,
    var type: String,
    var price: Int,
    var filled: Array<Status>,
    var unfilled: Array<Status>,
    var partiallyFilled: Array<Status>
)