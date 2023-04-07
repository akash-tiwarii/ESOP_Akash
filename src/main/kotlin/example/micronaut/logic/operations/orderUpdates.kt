package example.micronaut.logic.operations

import example.micronaut.controller.totalTaxCollected
import example.micronaut.controller.totalTransactionFee
import example.micronaut.exception.ApplicationException
import example.micronaut.model.AccountInfo
import example.micronaut.model.EsopType
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode


var totalTaxDeductionAfterTransaction: BigInteger = BigInteger.ZERO

val taxBracket: Map<EsopType, Map<String, BigDecimal>> = mapOf(
    EsopType.NORMAL to mapOf(
        "1-100" to 1.toBigDecimal(),
        "101-50k" to 1.25.toBigDecimal(),
        "greaterThan50k" to 1.5.toBigDecimal()
    ),
    EsopType.PERFORMANCE to mapOf(
        "1-100" to 2.toBigDecimal(),
        "101-50k" to 2.25.toBigDecimal(),
        "greaterThan50k" to 2.5.toBigDecimal()
    )
)


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

    if (esopType == EsopType.NORMAL) {
        tradeEsops(buyer.userName, seller.userName, saleQuantity.toLong(), salePrice, 0)
    } else {
        tradeEsops(buyer.userName, seller.userName, saleQuantity.toLong(), salePrice, 1)
    }


    val actualMoneyExchanged: BigInteger =
        calculateActualMoneyExchanged(saleQuantity, salePrice, percentOfMoneyToAdd)

    updateSellerWalletAndInventory(seller, saleQuantity, salePrice, actualMoneyExchanged, sellOrder?.esopType!!)

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
    salePrice: BigInteger,
    actualMoneyExchanged: BigInteger,
    esopType: EsopType
) {
    seller.inventory[esopType.ordinal].locked -= saleQuantity


    if (esopType == EsopType.NORMAL) {
        totalTaxDeductionAfterTransaction = calculateTaxNormalTransaction(saleQuantity, salePrice)
    }
    if (esopType == EsopType.PERFORMANCE) {
        totalTaxDeductionAfterTransaction = calculateTaxPerformanceTransaction(saleQuantity, salePrice)

    }

    seller.wallet.free += actualMoneyExchanged - totalTaxDeductionAfterTransaction
    totalTaxCollected += totalTaxDeductionAfterTransaction
}


fun calculateTaxNormalTransaction(saleQuantity: BigInteger, salePrice: BigInteger): BigInteger {
    if (saleQuantity > BigInteger.ZERO && saleQuantity <= 100.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            (saleQuantity.toBigDecimal() * salePrice.toBigDecimal() * (0.01).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
        totalTaxDeductionAfterTransaction = totalTaxDeductionAfterTransaction.min(BigInteger("20"))
    }
    if (saleQuantity > 100.toBigInteger() && saleQuantity <= 50000.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            ((saleQuantity.toBigDecimal() * salePrice.toBigDecimal()) * (0.0125).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
        totalTaxDeductionAfterTransaction = totalTaxDeductionAfterTransaction.min(BigInteger("20"))
    }
    if (saleQuantity > 50000.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            ((saleQuantity.toBigDecimal() * salePrice.toBigDecimal()) * (0.015).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
    }
    return totalTaxDeductionAfterTransaction
}

fun calculateTaxPerformanceTransaction(saleQuantity: BigInteger, salePrice: BigInteger): BigInteger {
    if (saleQuantity > BigInteger.ZERO && saleQuantity <= 100.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            (saleQuantity.toBigDecimal() * salePrice.toBigDecimal() * (0.02).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
        totalTaxDeductionAfterTransaction = totalTaxDeductionAfterTransaction.min(BigInteger("50"))
    }
    if (saleQuantity > 100.toBigInteger() && saleQuantity <= 50000.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            ((saleQuantity.toBigDecimal() * salePrice.toBigDecimal()) * (0.0225).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
    }
    if (saleQuantity > 50000.toBigInteger()) {
        totalTaxDeductionAfterTransaction =
            ((saleQuantity.toBigDecimal() * salePrice.toBigDecimal()) * (0.025).toBigDecimal()).setScale(
                0,
                RoundingMode.UP
            ).toBigInteger()
    }
    return totalTaxDeductionAfterTransaction
}

private fun updateBuyerWalletAndInventory(
    buyer: AccountInfo, salePrice: BigInteger, saleQuantity: BigInteger,
) {
    buyer.wallet.locked -= saleQuantity * salePrice
    buyer.wallet.locked -= saleQuantity * (buyOrders[0].price - salePrice)
    buyer.wallet.free += saleQuantity * (buyOrders[0].price - salePrice)
    buyer.inventory[0].free += saleQuantity
}

fun getTaxCollectedFromTransaction(): BigInteger {
    return totalTaxDeductionAfterTransaction
}

fun getTotalTaxCollected(): BigInteger {
    return totalTaxCollected
}

