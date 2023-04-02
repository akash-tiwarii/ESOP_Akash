package example.micronaut.model


data class OrderResponse(
    var orderId: String,
    var quantity: String,
    var type: String,
    var price: String
)