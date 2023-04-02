package example.micronaut.logic.operations

import example.micronaut.model.AccountInfo
import example.micronaut.model.Transaction
import java.math.BigInteger
import java.sql.Timestamp


val esopIdToTransaction: MutableMap<BigInteger, MutableList<Transaction>> = mutableMapOf()

fun getEsops(username: String): MutableMap<String,MutableList<BigInteger>>{
    var esopList : MutableMap<String,MutableList<BigInteger>> = mutableMapOf()
    for(user in usersArray){
        if(user.userName==username){
            esopList["NORMAL"] = user.inventory[0].esopsFree
            esopList["NORMAL"]?.addAll(user.inventory[0].esopsLocked)
            esopList["PERFORMANCE"] = user.inventory[1].esopsFree
            esopList["PERFORMANCE"]?.addAll(user.inventory[1].esopsLocked)
        }
    }
    return esopList
}

fun addEsopsFromFreeToLocked(user: AccountInfo, quantity: BigInteger, typeOfEsop: Int){

//    var tempEsopList: MutableList<BigInteger> = mutableListOf()
    for(i in 0 until quantity.toInt()){
//        tempEsopList.add(user.inventory[typeOfEsop].esopsFree.removeAt(i))
        user.inventory[typeOfEsop].esopsLocked.add(user.inventory[typeOfEsop].esopsFree.removeAt(0))
    }
}

fun tradeEsops(buyer: String, seller: String, quantity: Long, price: BigInteger,esopType:Int){
    val buyerUser = getAccountInfo(buyer)
    val sellerUser = getAccountInfo(seller)
    val transactionType = "Trade"
    val transaction = Transaction(buyerUser.userName,sellerUser.userName,transactionType,price.toString())
    for(i in 1..quantity){
        val esop = sellerUser.inventory[esopType].esopsLocked.removeAt(0)
        esopIdToTransaction[esop]?.add(transaction)
        buyerUser.inventory[0].esopsFree.add(esop)
    }

}