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

private const val TAX_SLAB_FOR_ONE_TO_HUNDRED = "1-100"

private const val TAX_SLAB_FOR_HUNDRED_TO_50K = "101-50k"

private const val TAX_SLAB_FOR_GREATER_THAN_50K = "greaterThan50k"

private const val TAX_PERCENTAGE = "taxPercentage"

private const val LIMIT = "limit"

val taxBracket: Map<EsopType, Map<String, Map<String,BigDecimal>>> = mapOf(
    EsopType.NORMAL to mapOf(
        TAX_SLAB_FOR_ONE_TO_HUNDRED to mapOf(TAX_PERCENTAGE to 0.01.toBigDecimal(), LIMIT to 20.toBigDecimal()),
        TAX_SLAB_FOR_HUNDRED_TO_50K to mapOf(TAX_PERCENTAGE to 0.0125.toBigDecimal(), LIMIT to 20.toBigDecimal()),
        TAX_SLAB_FOR_GREATER_THAN_50K to mapOf(TAX_PERCENTAGE to 0.015.toBigDecimal(), LIMIT to Long.MAX_VALUE.toBigDecimal())
    ),
    EsopType.PERFORMANCE to mapOf(
        TAX_SLAB_FOR_ONE_TO_HUNDRED to mapOf(TAX_PERCENTAGE to 0.02.toBigDecimal(), LIMIT to 50.toBigDecimal()),
        TAX_SLAB_FOR_HUNDRED_TO_50K to mapOf(TAX_PERCENTAGE to 0.0225.toBigDecimal(), LIMIT to Long.MAX_VALUE.toBigDecimal()),
        TAX_SLAB_FOR_GREATER_THAN_50K to mapOf(TAX_PERCENTAGE to 0.025.toBigDecimal(), LIMIT to Long.MAX_VALUE.toBigDecimal())
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

    totalTaxDeductionAfterTransaction = calculateTaxAfterTransaction(esopType, saleQuantity, salePrice)

    seller.wallet.free += actualMoneyExchanged - totalTaxDeductionAfterTransaction
    totalTaxCollected += totalTaxDeductionAfterTransaction
}


private fun calculateTaxAfterTransaction(
    esopType: EsopType,
    saleQuantity: BigInteger,
    salePrice: BigInteger
): BigInteger {


    val taxSlab = taxBracket[esopType]?.get(
        getTaxSlabForQuantity(
            quantity = saleQuantity
        )
    )
    totalTaxDeductionAfterTransaction =
        (saleQuantity.toBigDecimal() * salePrice.toBigDecimal() * (taxSlab?.get(TAX_PERCENTAGE)!!)).setScale(
            0,
            RoundingMode.UP
        ).toBigInteger()
    return totalTaxDeductionAfterTransaction.min(taxSlab[LIMIT]!!.toBigInteger())
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

// Have to change it later from ifs to
private fun getTaxSlabForQuantity(quantity: BigInteger): String {
    if (quantity <= BigInteger("100")) {
        return TAX_SLAB_FOR_ONE_TO_HUNDRED
    }
    if (quantity <= BigInteger("50000")) {
        return TAX_SLAB_FOR_HUNDRED_TO_50K
    }
    return TAX_SLAB_FOR_GREATER_THAN_50K
}
