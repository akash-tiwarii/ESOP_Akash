package example.micronaut.logic.checks

import example.micronaut.model.OrderFilled
import example.micronaut.model.OrderHistory

fun checkOrderPresence(orderHistory: List<OrderFilled>,orderId:Int) : OrderFilled {
    return orderHistory.find { orderFilled -> orderFilled.orderId.toInt() == orderId }
        ?: throw Exception("User has not placed a order with the given orderId")

}