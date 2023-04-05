package example.micronaut.logic.operations

import example.micronaut.controller.totalTransactionFee
import example.micronaut.exception.ApplicationException
import example.micronaut.model.AccountInfo
import example.micronaut.model.EsopType
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun orderUpdates(esopType: EsopType, saleQuantity: BigInteger, salePrice: BigInteger) {
    val percentOfMoneyToAdd = 0.98.toBigDecimal()

    val buyOrder = mappedOrders.keys.find { orderResponse -> orderResponse.orderId == buyOrders[0].orderId }
    val buyer = usersArray.find { orderResponse -> orderResponse.userName == mappedOrders[buyOrder] }
        ?: throw ApplicationException("ERROR SHOULDN'T HAVE BEEN DISPLAYED")
    updateBuyerWalletAndInventory(buyer, salePrice, saleQuantity)

    val sellOrder =
        if (esopType == EsopType.NORMAL)
            mappedOrders.keys.find { orderResponse -> orderResponse.orderId == sellOrdersNormal[0].orderId }
        else mappedOrders.keys.find { orderResponse -> orderResponse.orderId == sellOrdersPerformance[0].orderId }
            ?: throw ApplicationException("ERROR SHOULDN'T HAVE BEEN DISPLAYED")

    val seller = usersArray.find { orderResponse -> orderResponse.userName == mappedOrders[sellOrder] }
        ?: throw ApplicationException("ERROR SHOULDN'T HAVE BEEN DISPLAYED")

    if(esopType == EsopType.NORMAL){
        tradeEsops(buyer.userName,seller.userName,saleQuantity.toLong(),salePrice,0)
    }else{
        tradeEsops(buyer.userName,seller.userName,saleQuantity.toLong(),salePrice,1)
    }


    val actualMoneyExchanged: BigInteger =
        calculateActualMoneyExchanged(saleQuantity, salePrice, percentOfMoneyToAdd)

    updateSellerWalletAndInventory(seller, saleQuantity, actualMoneyExchanged, sellOrder?.esopType!!)

    totalTransactionFee += ((saleQuantity * salePrice) - actualMoneyExchanged)
}

private fun calculateActualMoneyExchanged(
    saleQuantity: BigInteger,
    salePrice: BigInteger,
    percentOfMoneyToAdd: BigDecimal
): BigInteger {
    return (saleQuantity.toBigDecimal() * salePrice.toBigDecimal() * percentOfMoneyToAdd).setScale(
        0, RoundingMode.DOWN
    ).toBigInteger()
}

private fun updateSellerWalletAndInventory(
    seller: AccountInfo,
    saleQuantity: BigInteger,
    actualMoneyExchanged: BigInteger,
    esopType: EsopType
) {
    seller.inventory[esopType.ordinal].locked -= saleQuantity
    seller.wallet.free += actualMoneyExchanged
}

private fun updateBuyerWalletAndInventory(
    buyer: AccountInfo,  salePrice: BigInteger,saleQuantity: BigInteger,
) {
    buyer.wallet.locked -= saleQuantity * salePrice
    buyer.wallet.locked -= saleQuantity * (buyOrders[0].price - salePrice)
    buyer.wallet.free += saleQuantity * (buyOrders[0].price - salePrice)
    buyer.inventory[0].free += saleQuantity
}
