package example.micronaut.model

import java.math.BigInteger

class OrderFilled(
    var orderId: String,
    var esopType: String,
    var quantity: String,
    var type: String,
    var price: String,
    var filled: ArrayList<Filled>
)

class Filled(var price: BigInteger, var quantity: BigInteger)