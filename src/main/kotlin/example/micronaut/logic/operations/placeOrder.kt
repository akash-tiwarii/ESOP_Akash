package example.micronaut.logic.operations

import example.micronaut.controller.noOfOrders
import example.micronaut.logic.checks.checkOrder
import example.micronaut.model.Order
import example.micronaut.model.OrderFilled
import example.micronaut.model.OrderResponse
import example.micronaut.model.OrderResponseSell


var mappedOrders = HashMap<OrderResponse, String>()
var buyOrders = mutableListOf<OrderResponse>()
var sellOrdersNormal = mutableListOf<OrderResponseSell>()
var mappedSellOrders = HashMap<OrderResponseSell, String>()

//newly added
var sellOrdersPerformance = mutableListOf<OrderResponseSell>()
var orderMap = HashMap<OrderFilled, String>()

fun placeOrder(ord: Order, username: String): Any {
    val validOrder = checkOrder(Order(ord.quantity, ord.type, ord.esopType, ord.price), username)
    if (validOrder == true) {
        noOfOrders++
        val newOrder: Any
        if (ord.type == "BUY") {
            newOrder = OrderResponse(noOfOrders.toString(), ord.quantity, ord.type, ord.price)
            orderMap[OrderFilled(
                noOfOrders.toString(),
                ord.esopType,
                newOrder.quantity,
                newOrder.type,
                newOrder.price,
                ArrayList()
            )] = username

            val orderDetails = OrderResponse(noOfOrders.toString(), ord.quantity, ord.type, ord.price)
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
            newOrder = OrderResponseSell(noOfOrders.toString(), ord.esopType, ord.quantity, ord.type, ord.price)
            orderMap[OrderFilled(
                noOfOrders.toString(),
                ord.esopType,
                newOrder.quantity,
                newOrder.type,
                newOrder.price,
                ArrayList()
            )] = username
            val orderDetails = OrderResponseSell(noOfOrders.toString(), ord.esopType, ord.quantity, ord.type, ord.price)

            mappedSellOrders[newOrder] = username
            if (ord.esopType == "NORMAL")
                sellOrdersNormal.add(orderDetails)
            else
                sellOrdersPerformance.add(orderDetails)

            if (sellOrdersNormal.size >= 2) {
                sellOrdersNormal = sellOrdersNormal.sortedWith(object : Comparator<OrderResponseSell> {
                    override fun compare(o1: OrderResponseSell, o2: OrderResponseSell): Int {
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
                sellOrdersPerformance = sellOrdersPerformance.sortedWith(object : Comparator<OrderResponseSell> {
                    override fun compare(o1: OrderResponseSell, o2: OrderResponseSell): Int {
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
        orderMatching(sellOrdersPerformance, buyOrders, "PERFORMANCE")
        orderMatching(sellOrdersNormal, buyOrders, "NORMAL")

        return newOrder
    } else
        return validOrder
}