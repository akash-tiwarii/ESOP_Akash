package example.micronaut.model

import java.math.BigInteger

class OrderFilled(
    var orderId: String,
    var esopType: EsopType?,
    var quantity: String,
    var type: OrderType,
    var price: String,
    var filled: ArrayList<Filled>
)

class Filled(var price: BigInteger, var quantity: BigInteger)