package example.micronaut.logic.operations

import example.micronaut.model.OrderResponse
import example.micronaut.model.OrderResponseSell

fun orderMatching(sellOrders: MutableList<OrderResponseSell>, buyOrders: MutableList<OrderResponse>, esopType: String) {
    while (!(sellOrders.size == 0 || buyOrders.size == 0) && buyOrders[0].price.toBigInteger() >= sellOrders[0].price.toBigInteger()) {
        if (buyOrders[0].quantity.toBigInteger() < sellOrders[0].quantity.toBigInteger()) {

            val salePrice = sellOrders[0].price.toBigInteger()
            val saleQuantity = buyOrders[0].quantity.toBigInteger()

            updateFilledOrders(sellOrders, esopType, salePrice, saleQuantity)

            orderUpdates(esopType, saleQuantity, salePrice)

            sellOrders[0].quantity = (sellOrders[0].quantity.toBigInteger() - saleQuantity).toString()
            buyOrders.removeAt(0)

        } else if (buyOrders[0].quantity.toBigInteger() > sellOrders[0].quantity.toBigInteger()) {
            val salePrice = sellOrders[0].price.toBigInteger()
            val saleQuantity = sellOrders[0].quantity.toBigInteger()

            updateFilledOrders(sellOrders, esopType, salePrice, saleQuantity)

            orderUpdates(esopType, saleQuantity, salePrice)
            buyOrders[0].quantity = (buyOrders[0].quantity.toBigInteger() - saleQuantity).toString()
            sellOrders.removeAt(0)

        } else {
            val saleQuantity = sellOrders[0].quantity.toBigInteger()
            val salePrice = sellOrders[0].price.toBigInteger()

            updateFilledOrders(sellOrders, esopType, salePrice, saleQuantity)

            orderUpdates(esopType, saleQuantity, salePrice)
            sellOrders.removeAt(0)
            buyOrders.removeAt(0)

        }
    }
}