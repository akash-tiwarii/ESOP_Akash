package example.micronaut.modelsAndOperations

import example.micronaut.logic.operations.*
import example.micronaut.model.EsopType
import example.micronaut.model.Order
import example.micronaut.model.OrderType
import example.micronaut.model.Register
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderRepositoryTest {

    @Test
    fun `order matching for buyer and seller`(){

        val buyer = registerUser(Register("john","doe","9999999999","johndoe1@gmail.com","john1"))
        val seller = registerUser(Register("john","doe","9999999998","johndoe2@gmail.com","john2"))

        addWallet("john1",1000.toBigInteger())
        addInventory("john2",EsopType.NORMAL,10.toBigInteger())

        val orderBuyer = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 100.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer,buyerUsername)

        val orderSeller = Order(quantity = 10.toBigInteger(), type = OrderType.SELL, esopType = EsopType.NORMAL, price = 100.toBigInteger())
        val sellerUsername = "john2"
        placeOrder(orderSeller,sellerUsername)

        orderMatching(sellOrdersNormal, buyOrders,EsopType.NORMAL)

        assertEquals(10,getAccountInfo(buyerUsername).inventory[0].esopsFree.size)
        assertEquals(980.toBigInteger(),getAccountInfo(sellerUsername).wallet.free)
    }



}