package example.micronaut.logic.operations

import example.micronaut.model.Filled
import example.micronaut.model.OrderResponse
import java.math.BigInteger


fun updateFilledOrders(
    sellOrders: MutableList<OrderResponse>,
    salePrice: BigInteger,
    saleQuantity: BigInteger
) {

    for (orderFilled in orderMap.keys) {
        if (orderFilled.orderId == buyOrders[0].orderId) {
            orderFilled.filled.add(Filled(salePrice, saleQuantity))
        } else if (orderFilled.orderId == sellOrders[0].orderId) {
            orderFilled.filled.add(Filled(salePrice, saleQuantity))
        }
    }
}