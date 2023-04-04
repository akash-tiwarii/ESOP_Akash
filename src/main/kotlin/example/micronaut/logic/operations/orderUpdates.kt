package example.micronaut.logic.operations

import example.micronaut.model.EsopType
import java.math.BigInteger
import java.math.RoundingMode

fun orderUpdates(esopType: EsopType, saleQuantity: BigInteger, salePrice: BigInteger) {

    var buyerUsername = ""

//    val buyer = mappedOrders.keys.find { orderResponse ->  orderResponse.orderId== buyOrders[0].orderId}
//    val buyerUser = usersArray.find { orderResponse -> orderResponse.userName== mappedOrders[buyer] }
//    val seller = mappedSellOrders.keys.find { orderResponse -> orderResponse.orderId==se }

    for (order in mappedOrders.keys) {
        if (order.orderId == buyOrders[0].orderId) {
            val username = mappedOrders[order]
            for (buyer in usersArray) {
                if (buyer.userName == username) {
                    buyerUsername = buyer.userName
                    buyer.wallet.locked -= saleQuantity * salePrice
                    buyer.wallet.locked -= saleQuantity * (buyOrders[0].price - salePrice)
                    buyer.wallet.free += saleQuantity * (buyOrders[0].price - salePrice)
                    buyer.inventory[0].free += saleQuantity

                    break
                }
            }
        }
    }

    var sellerUsername: String

    //updating sellOrder List
    if (esopType == EsopType.NORMAL) {
        for (order in mappedSellOrders.keys) {
            if (order.orderId == sellOrdersNormal[0].orderId) {
                val username = mappedSellOrders[order]
                for (seller in usersArray) {
                    if (seller.userName == username) {
                        sellerUsername = seller.userName
                        seller.inventory[0].locked -= saleQuantity

                        tradeEsops(buyerUsername, sellerUsername, saleQuantity.toLong(), salePrice, 0)

                        val amountFree =
                            ((0.98).toBigDecimal() * (saleQuantity * salePrice).toBigDecimal()).setScale(
                                0,
                                RoundingMode.UP
                            ).toBigInteger()
                        seller.wallet.free += amountFree
                        addTransactionFeeToOrganization(saleQuantity * salePrice - amountFree)
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

                        tradeEsops(buyerUsername, sellerUsername, saleQuantity.toLong(), salePrice, 1)

                        val amountFree =
                            ((0.98).toBigDecimal() * (saleQuantity * salePrice).toBigDecimal()).setScale(
                                0,
                                RoundingMode.UP
                            ).toBigInteger()
                        seller.wallet.free += amountFree
                        addTransactionFeeToOrganization(saleQuantity * salePrice - amountFree)
                        break
                    }
                }
            }
        }
    }
}