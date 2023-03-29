package example.micronaut.logic.operations

import com.example.logic.operations.*
import java.math.BigInteger

fun orderUpdates(esopType: String, saleQuantity:BigInteger, salePrice:BigInteger){

    for(order in mappedOrders.keys)
    {
        if(order.orderId == buyOrders[0].orderId)
        {
            val username = mappedOrders[order]
            for(buyer in usersArray)
            {
                if(buyer.userName==username)
                {
                    buyer.wallet.locked -= saleQuantity * salePrice
                    buyer.wallet.locked -= saleQuantity * (buyOrders[0].price.toBigInteger()- salePrice)
                    buyer.wallet.free += saleQuantity * (buyOrders[0].price.toBigInteger()- salePrice)
                    buyer.inventory[0].free += saleQuantity
                    break
                }
            }
        }
    }

    //updating sellOrder List
    if(esopType=="NORMAL")
    {
            for(order in mappedSellOrders.keys)
            {
                if(order.orderId == sellOrdersNormal[0].orderId)
                {
                    val username = mappedSellOrders[order]
                    for(seller in usersArray)
                    {
                        if(seller.userName==username)
                        {
                            seller.inventory[0].locked -= saleQuantity
                            var amountFree = ((0.98).toBigDecimal()*(saleQuantity* salePrice).toBigDecimal()).toBigInteger()
                            var transactionFee = ((0.02).toBigDecimal()*(saleQuantity* salePrice).toBigDecimal()).toBigInteger()
                            seller.wallet.free+=amountFree
                            addTransactionFeeToOrganization(transactionFee)
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
                        var amountFree = ((0.98).toBigDecimal()*(saleQuantity* salePrice).toBigDecimal()).toBigInteger()
                        var transactionFee = ((0.02).toBigDecimal()*(saleQuantity* salePrice).toBigDecimal()).toBigInteger()
                        seller.wallet.free+=amountFree
                        addTransactionFeeToOrganization(transactionFee)
                        break
                    }
                }
            }
        }
    }
}