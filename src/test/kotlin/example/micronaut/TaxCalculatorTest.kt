package example.micronaut

import example.micronaut.controller.totalTaxCollected
import example.micronaut.logic.operations.*
import example.micronaut.model.EsopType
import example.micronaut.model.Order
import example.micronaut.model.OrderType
import example.micronaut.model.Register
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class TaxCalculatorTest {

    @Test
    fun `should calculate the tax as 1 percent for quantity less than 100 and ESOP type is Normal`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 100.toBigInteger())

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

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(1.toBigInteger(), totalTax)

    }


    @Test
    fun `should calculate the tax as 1 percent for quantity less than 100 and ESOP type is Normal (calculated tax is greater than 20 min(20,calculated tax))`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 100.toBigInteger())

        val orderBuyer = Order(quantity = 100.toBigInteger(), type = OrderType.BUY, price = 100.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 80.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 100.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(20.toBigInteger(), totalTax)

    }

    @Test
    fun `should calculate the tax as 1 percent for quantity equal to 100 and ESOP type is Normal`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 100.toBigInteger())

        val orderBuyer = Order(quantity = 100.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 100.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(1.toBigInteger(), totalTax)

    }


    @Test
    fun `should calculate the tax as 1 and quarter percent for quantity greater than 100 but lesser than 50k and ESOP type is NORMAL`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 200.toBigInteger())

        val orderBuyer = Order(quantity = 180.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 150.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 10.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(19.toBigInteger(), totalTax)

    }

    @Test
    fun `should calculate the tax as 1 and quarter percent for quantity equal to 50000 and ESOP type is NORMAL`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 50000.toBigInteger())

        val orderBuyer = Order(quantity = 100000.toBigInteger(), type = OrderType.BUY, price = 15.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 50000.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 10.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(20.toBigInteger(), totalTax)

    }

    @Test
    fun `should calculate the tax as 1 and half percent for quantity greater than 50k and ESOP type is NORMAL`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 10000000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 60000.toBigInteger())

        val orderBuyer = Order(quantity = 60000.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 60000.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 10.toBigInteger()
        )

        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(9000.toBigInteger(), totalTax)

    }

    @Test
    fun `should return 0 as Tax amount if order matching does not happens`() {
        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 10000000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 60000.toBigInteger())

        val orderBuyer = Order(quantity = 60000.toBigInteger(), type = OrderType.BUY, price = 1.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 60000.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 10.toBigInteger()
        )

        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(0.toBigInteger(), totalTax)
    }

    @Test
    fun `should calculate the tax as 2 percent for quantity less than 100 and ESOP type is PERFORMANCE`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.PERFORMANCE, 100.toBigInteger())

        val orderBuyer = Order(quantity = 100.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 80.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.PERFORMANCE,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(2.toBigInteger(), totalTax)

    }

    @Test
    fun `should calculate the tax as 2 and a quarter percent for quantity greater than 100 and less than or equal to 50k and ESOP type is PERFORMANCE`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 100000.toBigInteger())
        addInventory("john2", EsopType.PERFORMANCE, 120.toBigInteger())

        val orderBuyer = Order(quantity = 150.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 120.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.PERFORMANCE,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(3.toBigInteger(), totalTax)

    }

    @Test
    fun `should calculate the tax as 2 and a half percent for quantity greater than 50k and ESOP type is PERFORMANCE`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 700000.toBigInteger())
        addInventory("john2", EsopType.PERFORMANCE, 60000.toBigInteger())

        val orderBuyer = Order(quantity = 60000.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyerUsername = "john1"
        placeOrder(orderBuyer, buyerUsername)

        val orderSeller = Order(
            quantity = 60000.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.PERFORMANCE,
            price = 1.toBigInteger()
        )


        val sellerUsername = "john2"
        placeOrder(orderSeller, sellerUsername)

        val totalTax = getTaxCollectedFromTransaction()

        assertEquals(1500.toBigInteger(), totalTax)

    }

    @Test
    fun `should get the total tax collected by the government so far`() {
        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        registerUser(Register("john", "doe", "9999999998", "johndoe2@gmail.com", "john2"))

        addWallet("john1", 700000.toBigInteger())
        addInventory("john2", EsopType.NORMAL, 120.toBigInteger())

        val buyOrder1 = Order(quantity = 110.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyer1Username = "john1"
        placeOrder(buyOrder1, buyer1Username)

        val sellOrder1 = Order(
            quantity = 120.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.NORMAL,
            price = 5.toBigInteger()
        )

        val seller1name = "john2"
        placeOrder(sellOrder1, seller1name)


        registerUser(Register("john", "doe", "7999999999", "johndoe3@gmail.com", "john3"))

        addWallet("john3", 700000.toBigInteger())

        val buyOrder2 = Order(quantity = 10.toBigInteger(), type = OrderType.BUY, price = 7.toBigInteger())
        val buyer2Username = "john3"
        placeOrder(buyOrder2, buyer2Username)

        val totalTax = getTotalTaxCollected()

        assertEquals(8.toBigInteger(), totalTax)

    }

    @Test
    fun `should get the tax of partially filled order`() {

        registerUser(Register("john", "doe", "7999999999", "johndoe3@gmail.com", "john3"))
        registerUser(Register("john", "doe", "9979999998", "johndoe4@gmail.com", "john4"))

        addWallet("john3", 1000000.toBigInteger())
        addInventory("john4", EsopType.PERFORMANCE, 150.toBigInteger())

        val buyOrder2 = Order(quantity = 50.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyer2Username = "john3"
        placeOrder(buyOrder2, buyer2Username)

        val orderSeller = Order(
            quantity = 60.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.PERFORMANCE,
            price = 5.toBigInteger()
        )
        val seller2Username = "john4"
        placeOrder(orderSeller, seller2Username)
        val totalTax = getTotalTaxCollected()

        assertEquals(5.toBigInteger(), totalTax)

    }

    @Test
    fun `should get 0 tax collected when no orders are placed`(){
        val totalTax = getTotalTaxCollected()

        assertEquals(0.toBigInteger(), totalTax)
    }

    @Test
    fun `should get 0 tax when only sell order is placed`() {

        registerUser(Register("john", "doe", "9979999998", "johndoe4@gmail.com", "john4"))

        addInventory("john4", EsopType.PERFORMANCE, 150.toBigInteger())

        val orderSeller = Order(
            quantity = 60.toBigInteger(),
            type = OrderType.SELL,
            esopType = EsopType.PERFORMANCE,
            price = 5.toBigInteger()
        )
        val seller2Username = "john4"
        placeOrder(orderSeller, seller2Username)
        val totalTax = getTotalTaxCollected()

        assertEquals(0.toBigInteger(), totalTax)

    }

    @Test
    fun `should get 0 tax when only buy order is placed`() {

        registerUser(Register("john", "doe", "7999999999", "johndoe3@gmail.com", "john3"))

        addWallet("john3", 1000000.toBigInteger())

        val buyOrder2 = Order(quantity = 50.toBigInteger(), type = OrderType.BUY, price = 10.toBigInteger())
        val buyer2Username = "john3"
        placeOrder(buyOrder2, buyer2Username)

        val totalTax = getTotalTaxCollected()

        assertEquals(0.toBigInteger(), totalTax)

    }

    @AfterEach
    fun tearDown() {
        usersArray.clear()
        mappedOrders.clear()
        orderMap.clear()
        buyOrders.clear()
        sellOrdersNormal.clear()
        sellOrdersPerformance.clear()
        totalTaxDeductionAfterTransaction = BigInteger.ZERO
        totalTaxCollected = BigInteger.ZERO

    }


}