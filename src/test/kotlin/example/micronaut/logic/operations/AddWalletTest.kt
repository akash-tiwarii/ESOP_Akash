package example.micronaut.logic.operations

import example.micronaut.model.Register
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddWalletTest{
    @Test
    fun `user able to add money in the wallet`(){
        val buyer = registerUser(Register("john","doe","9999999999","johndoe1@gmail.com","john1"))
        addWallet("john1",100.toBigInteger())
        Assertions.assertEquals(100.toBigInteger(), getAccountInfo("john1").wallet.free)
    }
    @Test
    fun `user should not able to add money in the wallet if user not registered`(){
        val user = getAccountInfo("user")


    }

}