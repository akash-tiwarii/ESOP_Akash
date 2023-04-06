package example.micronaut.logic.operations

import example.micronaut.model.AddInventory
import example.micronaut.model.EsopType
import example.micronaut.model.Register
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AddInventoryTest {

    @Test
    fun `user able to add normal ESOPs in the inventory`() {
        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        addInventory("john1", EsopType.NORMAL, 10.toBigInteger())
        assertEquals(10, getAccountInfo("john1").inventory[0].esopsFree.size)
    }

    @Test
    fun `user able to add performance ESOPs in the inventory`() {
        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        addInventory("john1", EsopType.PERFORMANCE, 10.toBigInteger())
        assertEquals(10, getAccountInfo("john1").inventory[1].esopsFree.size)
    }

    @Test
    fun `user should not able to add Normal ESOPs in the inventory if user not registered`() {
        try {
            addInventory("user", EsopType.NORMAL, 10.toBigInteger())
        } catch (e: Exception) {
            assertEquals("User not registered", e.message)
        }
    }

    @Test
    fun `user should not able to add Performance ESOPs in the inventory if user not registered`() {
        try {
            addInventory("user", EsopType.PERFORMANCE, 10.toBigInteger())
        } catch (e: Exception) {
            assertEquals("User not registered", e.message)
        }
    }

    @Test
    fun `user should not able to add Normal ESOPs in the inventory if quantity less than 0`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateInventory(AddInventory(EsopType.NORMAL, "-100"), user.userName)
        } catch (e: Exception) {
            assertEquals("Inventory should be a positive Integer not exceeding 9223372036854775806", e.message)
        }
    }

    @Test
    fun `user should not able to add Normal ESOPs in the inventory if quantity greater than 9223372036854775806`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateInventory(AddInventory(EsopType.NORMAL, "98765432197654321987654321"), user.userName)
        } catch (e: Exception) {
            assertEquals("Inventory should be a positive Integer not exceeding 9223372036854775806", e.message)
        }
    }

    @Test
    fun `user should not able to add Performance ESOPs in the inventory if quantity less than 0`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateInventory(AddInventory(EsopType.PERFORMANCE, "-100"), user.userName)
        } catch (e: Exception) {
            assertEquals("Inventory should be a positive Integer not exceeding 9223372036854775806", e.message)
        }
    }

    @Test
    fun `user should not able to add Performance ESOPs in the inventory if quantity greater than 9223372036854775806`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateInventory(AddInventory(EsopType.PERFORMANCE, "98765432197654321987654321"), user.userName)
        } catch (e: Exception) {
            assertEquals("Inventory should be a positive Integer not exceeding 9223372036854775806", e.message)
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