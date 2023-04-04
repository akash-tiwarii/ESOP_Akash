package example.micronaut.model


data class OrderResponse(
    var orderId: String,
    var quantity: String,
    var esopType: EsopType? = null,
    var type: OrderType,
    var price: String
)