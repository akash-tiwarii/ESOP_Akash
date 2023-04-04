package example.micronaut.logic.operations

import example.micronaut.model.Filled
import example.micronaut.model.OrderResponse
import java.math.BigInteger


fun updateFilledOrders(
    sellOrders: MutableList<OrderResponse>,
    salePrice: BigInteger,
    saleQuantity: BigInteger
) {
    orderMap.keys.find { it.orderId == buyOrders[0].orderId }?.filled
        ?.add(Filled(salePrice, saleQuantity))
    orderMap.keys.find { it.orderId == sellOrders[0].orderId }
        ?.filled?.add(Filled(salePrice, saleQuantity))
}