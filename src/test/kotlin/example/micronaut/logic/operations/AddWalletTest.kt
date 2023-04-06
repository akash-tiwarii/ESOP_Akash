package example.micronaut.logic.operations

import example.micronaut.model.AddWallet
import example.micronaut.model.Register
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AddWalletTest {
    @Test
    fun `user able to add money in the wallet`() {
        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        addWallet("john1", 100.toBigInteger())
        assertEquals(100.toBigInteger(), getAccountInfo("john1").wallet.free)
    }

    @Test
    fun `user should not able to add money in the wallet if user not registered`() {
        try {
            getAccountInfo("user")
        } catch (e: Exception) {
            assertEquals("User not registered", e.message)
        }
    }

    @Test
    fun `user should not able to add money in the wallet if amount less than 0`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateWallet(AddWallet("-100"), user.userName)
        } catch (e: Exception) {
            assertEquals("Amount should be a positive Integer not exceeding 9223372036854775806", e.message)
        }
    }

    @Test
    fun `user should not able to add money in the wallet if amount greater than 9223372036854775806`() {

        registerUser(Register("john", "doe", "9999999999", "johndoe1@gmail.com", "john1"))
        val user = getAccountInfo("john1")
        try {
            validateWallet(AddWallet("9876545678909876565456765987654321"), user.userName)
        } catch (e: Exception) {
            assertEquals("Amount should be a positive Integer not exceeding 9223372036854775806", e.message)
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