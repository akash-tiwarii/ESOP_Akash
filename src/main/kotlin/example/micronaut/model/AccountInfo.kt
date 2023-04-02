package example.micronaut.model

import java.math.BigInteger

data class Wallet(
    var free: BigInteger,
    var locked: BigInteger
)


data class Inventory(
    var type: String,
    var free: BigInteger,
    var locked: BigInteger,

//    @JsonIgnore
    var esopsFree: MutableList<BigInteger> = mutableListOf(),
    var esopsLocked: MutableList<BigInteger> = mutableListOf()
)


data class AccountInfo(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var userName: String,
    var email: String,
    var wallet: Wallet,
    var inventory: ArrayList<Inventory>
)
