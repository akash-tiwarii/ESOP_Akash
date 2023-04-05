package example.micronaut.logic.operations

import example.micronaut.controller.noOfOrders
import example.micronaut.logic.checks.hasSuccessfullyLockMoneyAndInventoryForValidOrder
import example.micronaut.model.*


var mappedOrders = HashMap<OrderResponse, String>()
var buyOrders = mutableListOf<OrderResponse>()
var sellOrdersNormal = mutableListOf<OrderResponse>()
var mappedSellOrders = HashMap<OrderResponse, String>()

//newly added
var sellOrdersPerformance = mutableListOf<OrderResponse>()
var orderMap = HashMap<OrderFilled, String>()

fun placeOrder(ord: Order, username: String): OrderResponse {
    hasSuccessfullyLockMoneyAndInventoryForValidOrder(Order(ord.quantity, ord.type, ord.esopType, ord.price), username)
    noOfOrders++

    val newOrder = OrderResponse(noOfOrders.toString(), ord.quantity, ord.esopType, ord.type, ord.price)

    orderMap[OrderFilled(
        noOfOrders.toString(),
        ord.esopType,
        newOrder.quantity,
        newOrder.type,
        newOrder.price,
        ArrayList(),
    )] = username

    val orderDetails = OrderResponse(noOfOrders.toString(), ord.quantity,ord.esopType, ord.type, ord.price)

    mappedOrders[newOrder] = username

    if (ord.type == OrderType.BUY) insertInBuyOrders(orderDetails)
    else {

        if (ord.esopType == EsopType.NORMAL) addToNormalSellOrders(orderDetails)
        else addToPerformanceSellOrders(orderDetails)
    }
    orderMatching(sellOrdersPerformance, buyOrders, EsopType.PERFORMANCE)
    orderMatching(sellOrdersNormal, buyOrders, EsopType.NORMAL)

    return newOrder
}

private fun insertInBuyOrders(order: OrderResponse) {
    buyOrders.add(order)
    buyOrders.sortByDescending { it.price }

}

private fun addToNormalSellOrders(order: OrderResponse) {
    sellOrdersNormal.add(order)
    sellOrdersNormal.sortByDescending { it.price }
}

private fun addToPerformanceSellOrders(order: OrderResponse) {
    sellOrdersPerformance.add(order)
}