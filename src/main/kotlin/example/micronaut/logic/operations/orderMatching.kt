package example.micronaut.logic.operations

import example.micronaut.model.EsopType
import example.micronaut.model.OrderResponse

fun orderMatching(sellOrders: MutableList<OrderResponse>, buyOrders: MutableList<OrderResponse>, esopType: EsopType) {
    while (!(sellOrders.size == 0 || buyOrders.size == 0) && buyOrders[0].price.toBigInteger() >= sellOrders[0].price.toBigInteger()) {

        val salePrice = sellOrders[0].price.toBigInteger()
        val saleQuantity = buyOrders[0].quantity.toBigInteger()
        updateFilledOrders(sellOrders, salePrice, saleQuantity)
        orderUpdates(esopType, saleQuantity, salePrice)

        if (buyOrders[0].quantity.toBigInteger() < sellOrders[0].quantity.toBigInteger()) {
            sellOrders[0].quantity = (sellOrders[0].quantity.toBigInteger() - saleQuantity).toString()
            buyOrders.removeAt(0)
        } else if (buyOrders[0].quantity.toBigInteger() > sellOrders[0].quantity.toBigInteger()) {
            buyOrders[0].quantity = (buyOrders[0].quantity.toBigInteger() - saleQuantity).toString()
            sellOrders.removeAt(0)
        } else {
            sellOrders.removeAt(0)
            buyOrders.removeAt(0)
        }
    }
}