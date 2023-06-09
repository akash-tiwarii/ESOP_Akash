package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkUserPresence
import example.micronaut.model.AccountInfo
import example.micronaut.model.Transaction
import example.micronaut.model.TransactionType
import example.micronaut.model.UserEsops
import java.math.BigInteger


val esopIdToTransaction: MutableMap<BigInteger, MutableList<Transaction>> = mutableMapOf()

fun getEsops(username: String): MutableList<UserEsops> {
    val esopList: MutableList<BigInteger> = mutableListOf()
    val userEsopList: MutableList<UserEsops> = mutableListOf()
    if (!checkUserPresence(username)) {
        val errorObject = Error(mutableListOf())
        errorObject.messages.add("User not registered")
        throw ApplicationException(errorObject.messages.joinToString(separator = ","))
    }
    for (user in usersArray) {
        if (user.userName == username) {
            esopList.addAll(user.inventory[0].esopsFree)
            esopList.addAll(user.inventory[0].esopsLocked)
            esopList.addAll(user.inventory[1].esopsFree)
            esopList.addAll(user.inventory[1].esopsLocked)

            for (esop in esopList) {
                val transaction = esopIdToTransaction.getValue(esop).last()
                userEsopList.add(
                    UserEsops(
                        esop.toString(),
                        username,
                        transaction.transactionType,
                        transaction.cost,
                        transaction.timestamp,
                        transaction.transactionId.toString()
                    )
                )
            }

        }
    }
    return userEsopList


}

fun addEsopsFromFreeToLocked(user: AccountInfo, quantity: BigInteger, typeOfEsop: Int) {

    for (i in 0 until quantity.toInt()) {
        user.inventory[typeOfEsop].esopsLocked.add(user.inventory[typeOfEsop].esopsFree.removeAt(0))
    }
}

fun tradeEsops(buyer: String, seller: String, quantity: Long, price: BigInteger, esopType: Int) {
    val buyerUser = getAccountInfo(buyer)
    val sellerUser = getAccountInfo(seller)
    val transaction = Transaction(buyerUser.userName, sellerUser.userName, TransactionType.TRADE, price.toString())
    for (i in 1..quantity) {
        val esop = sellerUser.inventory[esopType].esopsLocked.removeAt(0)
        esopIdToTransaction[esop]?.add(transaction)
        buyerUser.inventory[0].esopsFree.add(esop)
    }

}