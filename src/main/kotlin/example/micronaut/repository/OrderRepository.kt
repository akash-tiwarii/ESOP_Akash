package example.micronaut.repository

import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkOrderPresence
import example.micronaut.logic.operations.*
import example.micronaut.model.*
import jakarta.inject.Singleton
import java.math.BigInteger

@Singleton
class OrderRepository {

    fun cancelOrder(order: OrderCancel, username: String, orderId: Int): Message {

        validUsername(username)
        validQuantity(order.quantity)

        val orderHistory = getHistoryOf(username)
        val actualOrder = checkOrderPresence(orderHistory, orderId)
        val quantityFilled = quantityPartiallyFilled(actualOrder)
        val quantityCancelled = quantityCancelled(actualOrder)
        val quantityRemaining = actualOrder.quantity - (quantityFilled + quantityCancelled)
        if (quantityRemaining < order.quantity)
            throw ApplicationException("Cancelled order quantity cannot exceed remaining quantity : $quantityRemaining")

        if (actualOrder.type == OrderType.BUY) {
            removeFromOrders(orderId, order.quantity, buyOrders)
            removeFromLockedWallet(username, order.quantity, actualOrder.price)
        }
        if (actualOrder.type == OrderType.SELL) {
            removeFromLockedInventory(username, order.quantity, actualOrder.esopType!!)

            if (actualOrder.esopType == EsopType.NORMAL)
                removeFromOrders(orderId, order.quantity, sellOrdersNormal)
            if (actualOrder.esopType == EsopType.PERFORMANCE) {
                removeFromPerfSellOrders(orderId, order.quantity)
            }
        }

        updateCancellation(order.quantity, orderId)
        return Message(" ${order.quantity} ESOPS removed from the order $orderId")

    }

    private fun validUsername(username: String) {
        getAccountInfo(username)
    }

    private fun validQuantity(quantity: BigInteger) {
        if (quantity < BigInteger.ZERO || quantity > Long.MAX_VALUE.toBigInteger()) {
            throw ApplicationException("Quantity should be a positive Integer and less than 9_223_372_036_854_775_807")
        }
    }


    private fun quantityPartiallyFilled(order: OrderFilled): BigInteger {
        var quantity = BigInteger.ZERO
        for (entity in order.filled) {
            quantity += entity.quantity
        }
        return quantity
    }

    private fun quantityCancelled(order: OrderFilled): BigInteger {
        var quantity = BigInteger.ZERO
        for (entity in order.filled) {
            quantity += entity.quantity
        }
        return quantity
    }

    private fun removeFromOrders(id: Int, quantity: BigInteger, orderList: MutableList<OrderResponse>) {

        val order = orderList.find { orderResponse -> orderResponse.orderId == id.toString() }
            ?: throw ApplicationException("ERRORS SHOULDN'T HAVE BEEN SHOWN")
        order.quantity = order.quantity - quantity

        if (order.quantity == BigInteger.ZERO) {
            orderList.remove(order)
            return
        }
        orderList.replaceAll { orderResponse ->
            if (orderResponse.orderId == id.toString()) order else orderResponse
        }
        return
    }


    private fun removeFromLockedInventory(username: String, quantity: BigInteger, esopType: EsopType) {
        val user = getAccountInfo(username)
        user.inventory[esopType.ordinal].locked -= quantity
        user.inventory[esopType.ordinal].free += quantity
    }

    private fun removeFromLockedWallet(username: String, quantity: BigInteger, price: BigInteger) {
        val user = getAccountInfo(username)
        user.wallet.locked -= quantity * price
        user.wallet.free += quantity * price
    }

    private fun removeFromPerfSellOrders(id: Int, quantity: BigInteger) {
        for (order in sellOrdersPerformance) {
            if (order.orderId == id.toString()) {
                if (order.quantity == quantity) {
                    sellOrdersPerformance.remove(order)
                    return
                } else {
                    order.quantity = order.quantity - quantity
                    return
                }
            }
        }
        throw ApplicationException("Order not found")
    }

    private fun updateCancellation(quantity: BigInteger, orderId: Int) {

        val order = orderMap.keys.find { orderFilled -> orderFilled.orderId == orderId.toString() }
            ?: throw ApplicationException("ERROR SHOULDN'T HAVE BEEN DISPLAYED")
        order.cancelled.add(Cancelled(quantity))

    }

}

