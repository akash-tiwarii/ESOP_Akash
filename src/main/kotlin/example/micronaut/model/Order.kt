package example.micronaut.model

import java.math.BigInteger

data class Order(
    var quantity: BigInteger,
    var type: OrderType,
    var esopType: EsopType? = null,
    var price: BigInteger
)