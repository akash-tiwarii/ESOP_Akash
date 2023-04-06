package example.micronaut.modelsAndOperations

import example.micronaut.controller.totalTransactionFee
import example.micronaut.logic.operations.*
import example.micronaut.model.EsopType
import example.micronaut.model.Order
import example.micronaut.model.OrderType
import example.micronaut.model.Register
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderRepositoryTest {

    @Test
    fun `order matching for buyer and seller`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 1000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 10.toBigInteger())

        val orderBuyer = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 100.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 10.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 100.toBigInteger()
        )
        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        orderMatching(sellOrdersNormal, buyOrders, EsopType.NORMAL)

        assertEquals(10, getAccountInfo(buyerUsername).inventory[0].esopsFree.size)
        assertEquals(980.toBigInteger(), getAccountInfo(sellerUsername).wallet.free)
    }

    @Test
    fun `organization should collect 2 Percent transaction fee after teh transactions`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 1000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 10.toBigInteger())

        val orderBuyer = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 20.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 10.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 18.toBigInteger()
        )
        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        orderMatching(sellOrdersNormal, buyOrders, EsopType.NORMAL)
        assertEquals(4.toBigInteger(), totalTransactionFee)

    }

    @Test
    fun `partial order matching for buyer when seller is present`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 1000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 10.toBigInteger())

        val orderBuyer = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 20.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 5.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 18.toBigInteger()
        )
        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        assertEquals(5, getAccountInfo(buyerUsername).inventory[0].esopsFree.size)
        assertEquals(100.toBigInteger(), getAccountInfo(buyerUsername).wallet.locked)
        assertEquals(810.toBigInteger(), getAccountInfo(buyerUsername).wallet.free)
        assertEquals(88.toBigInteger(), getAccountInfo(sellerUsername).wallet.free)
    }


    @Test
    fun `partial order matching for seller when buyer is present`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 1000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 10.toBigInteger())

        val orderBuyer = Order(quantity = 5.toBigInteger(), type = OrderType.BUY, price = 20.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 10.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 18.toBigInteger()
        )
        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        assertEquals(5, getAccountInfo(buyerUsername).inventory[0].esopsFree.size)
        assertEquals(5.toBigInteger(), getAccountInfo(sellerUsername).inventory[0].locked)
        assertEquals(910.toBigInteger(), getAccountInfo(buyerUsername).wallet.free)
        assertEquals(88.toBigInteger(), getAccountInfo(sellerUsername).wallet.free)

    }


    @AfterEach
    fun teardown() {
        usersArray.clear()
        mappedOrders.clear()
        orderMap.clear()
        buyOrders.clear()
        sellOrdersNormal.clear()
        sellOrdersPerformance.clear()
        totalTransactionFee = 0.toBigInteger()
    }

    @Test
    fun `tax deduction from sellers account after every successfull transactions happened`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 80.toBigInteger())

        val orderBuyer = Order(quantity = 100.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 80.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        assertEquals(77.toBigInteger(), getAccountInfo(sellerUsername).wallet.free)

    }

    @Test
    fun `Should get the total tax collected by the government so far`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 80.toBigInteger())

        val orderBuyer = Order(quantity = 100.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 80.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        assertEquals(77.toBigInteger(), getAccountInfo(sellerUsername).wallet.free)

    }
}