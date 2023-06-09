package example.micronaut.model

import java.math.BigInteger

class OrderFilled(
    var orderId: String,
    var esopType: EsopType?,
    var quantity: BigInteger,
    var type: OrderType,
    var price: BigInteger,
    var filled: ArrayList<Filled>,
    var cancelled: ArrayList<Cancelled>
)

class Filled(var price: BigInteger, var quantity: BigInteger)
class Cancelled(var quantity: BigInteger)