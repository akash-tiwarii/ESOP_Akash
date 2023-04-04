package example.micronaut.model

import java.math.BigInteger


data class OrderResponse(
    var orderId: String,
    var quantity: BigInteger,
    var esopType: EsopType? = null,
    var type: OrderType,
    var price: BigInteger
)