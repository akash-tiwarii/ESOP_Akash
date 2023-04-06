package example.micronaut.logic.operations

import example.micronaut.model.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlaceOrderTest{

    @Test
    fun `not able to place BUY order if the free amount is Insufficient`(){

        try {
            val buyer = registerUser(Register("john","doe","9999999999","johndoe1@gmail.com","john1"))
            val seller = registerUser(Register("john","doe","9999999998","johndoe2@gmail.com","john2"))

            addWallet("john1",100.toBigInteger())

            val orderBuyer = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 100.toBigInteger())
            val buyerUsername = "john1"
            placeOrder(orderBuyer,buyerUsername)
        }catch (e: Exception){
            assertEquals("Insufficient balance",e.message)

        }


    }

    @Test
    fun `not able to place SELL order if the free Normal ESOPs are Insufficient`(){

        try {
            val seller = registerUser(Register("john","doe","9999999999","johndoe1@gmail.com","john1"))
            addInventory("john1",EsopType.NORMAL,100.toBigInteger())
            val orderSeller = Order(quantity = 10.toBigInteger(), esopType = EsopType.NORMAL ,type = OrderType.SELL, price = 100.toBigInteger())
            val sellerUsername = "john1"
            placeOrder(orderSeller,sellerUsername)

        }catch (e: Exception){
            assertEquals("Insufficient ESOPs in inventory",e.message)

        }


    }

    @Test
    fun `not able to place SELL order if the free Performance ESOPs are Insufficient`(){

        try {
            val seller = registerUser(Register("john","doe","9999999999","johndoe1@gmail.com","john1"))
            addInventory("john1",EsopType.PERFORMANCE,100.toBigInteger())
            val orderSeller = Order(quantity = 10.toBigInteger(), esopType = EsopType.NORMAL ,type = OrderType.SELL, price = 100.toBigInteger())
            val sellerUsername = "john1"
            placeOrder(orderSeller,sellerUsername)

        }catch (e: Exception){
            assertEquals("Insufficient ESOPs in inventory",e.message)

        }


    }

    @AfterEach
    fun teardown() {
        usersArray.clear()
        mappedOrders.clear()
        orderMap.clear()
        buyOrders.clear()
        sellOrdersNormal.clear()
        sellOrdersPerformance.clear()

    }

}