package example.micronaut.logic.operations

import example.micronaut.controller.noOfOrders
import example.micronaut.logic.checks.checkOrder
import example.micronaut.model.*


var mappedOrders = HashMap<OrderResponse, String>()
var buyOrders = mutableListOf<OrderResponse>()
var sellOrdersNormal = mutableListOf<OrderResponse>()
var mappedSellOrders = HashMap<OrderResponse, String>()

//newly added
var sellOrdersPerformance = mutableListOf<OrderResponse>()
var orderMap = HashMap<OrderFilled, String>()

fun placeOrder(ord: Order, username: String): Any {
    val validOrder = checkOrder(Order(ord.quantity, ord.type, ord.esopType, ord.price), username)
    if (validOrder == true) {
        noOfOrders++
        val newOrder: Any
        if (ord.type == OrderType.BUY) {
            newOrder = OrderResponse(
                orderId = noOfOrders.toString(), quantity = ord.quantity, type = ord.type, price = ord.price
            )
            orderMap[OrderFilled(
                orderId = noOfOrders.toString(),
                esopType = ord.esopType,
                quantity = newOrder.quantity,
                type = newOrder.type,
                price = newOrder.price,
                filled = ArrayList()
            )] = username

            val orderDetails = OrderResponse(
                orderId = noOfOrders.toString(), quantity = ord.quantity, type = ord.type, price = ord.price
            )
            mappedOrders[newOrder] = username

            buyOrders.add(orderDetails)
            if (buyOrders.size >= 2) {
                buyOrders = buyOrders.sortedWith(object : Comparator<OrderResponse> {
                    override fun compare(o1: OrderResponse, o2: OrderResponse): Int {
                        if (o1.price < o2.price) {
                            return 1
                        }
                        if (o1.price == o2.price) {
                            return o1.orderId.toInt() - o2.orderId.toInt()
                        }
                        return -1
                    }
                }).toMutableList()
            }

        } else {
            newOrder = OrderResponse(noOfOrders.toString(), ord.quantity, ord.esopType, ord.type, ord.price)
            orderMap[OrderFilled(
                noOfOrders.toString(), ord.esopType, newOrder.quantity, newOrder.type, newOrder.price, ArrayList()
            )] = username
            val orderDetails = OrderResponse(noOfOrders.toString(), ord.quantity, ord.esopType, ord.type, ord.price)

            mappedSellOrders[newOrder] = username
            if (ord.esopType == EsopType.NORMAL) sellOrdersNormal.add(orderDetails)
            else sellOrdersPerformance.add(orderDetails)

            if (sellOrdersNormal.size >= 2) {
                sellOrdersNormal = sellOrdersNormal.sortedWith(object : Comparator<OrderResponse> {
                    override fun compare(o1: OrderResponse, o2: OrderResponse): Int {
                        if (o1.price > o2.price) {
                            return 1
                        }
                        if (o1.price == o2.price) {
                            return o1.orderId.toInt() - o2.orderId.toInt()
                        }
                        return -1
                    }
                }).toMutableList()
            }

            if (sellOrdersPerformance.size >= 2) {
                sellOrdersPerformance = sellOrdersPerformance.sortedWith(object : Comparator<OrderResponse> {
                    override fun compare(o1: OrderResponse, o2: OrderResponse): Int {
                        if (o1.price > o2.price) {
                            return 1
                        }
                        if (o1.price == o2.price) {
                            return o1.orderId.toInt() - o2.orderId.toInt()
                        }
                        return -1
                    }
                }).toMutableList()
            }
        }
        orderMatching(sellOrdersPerformance, buyOrders, EsopType.PERFORMANCE)
        orderMatching(sellOrdersNormal, buyOrders, EsopType.NORMAL)

        return newOrder
    } else return validOrder
}