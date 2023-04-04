package example.micronaut.logic.operations

import example.micronaut.model.EsopType
import example.micronaut.model.OrderResponse

fun orderMatching(sellOrders: MutableList<OrderResponse>, buyOrders: MutableList<OrderResponse>, esopType: EsopType) {
    while (!(sellOrders.size == 0 || buyOrders.size == 0) && buyOrders[0].price >= sellOrders[0].price) {

        val salePrice = sellOrders[0].price
        val saleQuantity = buyOrders[0].quantity
        updateFilledOrders(sellOrders, salePrice, saleQuantity)
        orderUpdates(esopType, saleQuantity, salePrice)

        if (buyOrders[0].quantity < sellOrders[0].quantity) {
            sellOrders[0].quantity = (sellOrders[0].quantity - saleQuantity)
            buyOrders.removeAt(0)
        } else if (buyOrders[0].quantity > sellOrders[0].quantity) {
            buyOrders[0].quantity = (buyOrders[0].quantity - saleQuantity)
            sellOrders.removeAt(0)
        } else {
            sellOrders.removeAt(0)
            buyOrders.removeAt(0)
        }
    }
}