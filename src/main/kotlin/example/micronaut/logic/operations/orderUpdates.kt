package example.micronaut.logic.operations

import java.math.BigInteger
import java.math.RoundingMode
fun orderUpdates(esopType: String, saleQuantity: BigInteger, salePrice: BigInteger) {

    var buyerUsername : String = ""

    for (order in mappedOrders.keys) {
        if (order.orderId == buyOrders[0].orderId) {
            val username = mappedOrders[order]
            for (buyer in usersArray) {
                if (buyer.userName == username) {
                    buyerUsername= buyer.userName
                    buyer.wallet.locked -= saleQuantity * salePrice
                    buyer.wallet.locked -= saleQuantity * (buyOrders[0].price.toBigInteger() - salePrice)
                    buyer.wallet.free += saleQuantity * (buyOrders[0].price.toBigInteger() - salePrice)
                    buyer.inventory[0].free += saleQuantity

                    break
                }
            }
        }
    }

    var sellerUsername : String

    //updating sellOrder List
    if (esopType == "NORMAL") {
        for (order in mappedSellOrders.keys) {
            if (order.orderId == sellOrdersNormal[0].orderId) {
                val username = mappedSellOrders[order]
                for (seller in usersArray) {
                    if (seller.userName == username) {
                        sellerUsername = seller.userName
                        seller.inventory[0].locked -= saleQuantity

                        tradeEsops(buyerUsername,sellerUsername, saleQuantity.toLong(),salePrice,0)

                        var amountFree =
                            ((0.98).toBigDecimal() * (saleQuantity * salePrice).toBigDecimal()).setScale(0,RoundingMode.UP).toBigInteger()
                        seller.wallet.free += amountFree
                        addTransactionFeeToOrganization(saleQuantity * salePrice-amountFree)
                    }
                }
            }
        }
    }

    //change need to be done here
    else {
        for (order in mappedSellOrders.keys) {
            if (order.orderId == sellOrdersPerformance[0].orderId) {
                val username = mappedSellOrders[order]
                for (seller in usersArray) {
                    if (seller.userName == username) {
                        sellerUsername = seller.userName
                        seller.inventory[1].locked -= saleQuantity

                        tradeEsops(buyerUsername,sellerUsername, saleQuantity.toLong(),salePrice,1)

                        var amountFree =
                            ((0.98).toBigDecimal() * (saleQuantity * salePrice).toBigDecimal()).setScale(0,RoundingMode.UP).toBigInteger()
                        seller.wallet.free += amountFree
                        addTransactionFeeToOrganization(saleQuantity * salePrice-amountFree)
                        break
                    }
                }
            }
        }
    }
}