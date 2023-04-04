package example.micronaut.logic.checks

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.operations.addEsopsFromFreeToLocked
import example.micronaut.logic.operations.getAccountInfo
import example.micronaut.model.AccountInfo
import example.micronaut.model.EsopType
import example.micronaut.model.Order
import example.micronaut.model.OrderType
import java.math.BigInteger

fun hasSuccessfullyLockMoneyAndInventoryForValidOrder(order: Order, username: String): Any {

    val error = Error(mutableListOf())
    val price = order.price
    val quantity = order.quantity

    if (order.type == OrderType.SELL) {
        error.messages.addAll(validateSellOrder(order))
    } else {
        error.messages.addAll(validateBuyOrder(order))
    }

    if (error.messages.size != 0) {
        return error
    }

    val user = getAccountInfo(username)

    if (order.type == OrderType.BUY) {
        if (quantity * price <= user.wallet.free) {
            updateWalletForBuyer(user, quantity, price)
            return true
        } else {
            error.messages.add("Insufficient balance")
        }
    } else if (order.type == OrderType.SELL) {
        if (order.esopType == EsopType.NORMAL) {
            if (quantity > user.inventory[0].free)
                error.messages.add("Insufficient ESOPs in inventory")
            if (quantity <= user.inventory[0].free) {
                updateInventoryForNormalSeller(user, quantity)
                addEsopsFromFreeToLocked(user, quantity, 0)
                return true
            }
        } else {
            if (quantity > user.inventory[1].free)
                error.messages.add("Insufficient ESOPs in inventory")
            if (quantity <= user.inventory[1].free) {
                updateInventoryForPerformanceSeller(user, quantity)
                addEsopsFromFreeToLocked(user, quantity, 1)
                return true
            }
        }
    } else {
        error.messages.add("Type must be either BUY or SELL only")
    }
    throw ApplicationException(error.messages.joinToString(separator = ","))

}

fun updateInventoryForPerformanceSeller(user: AccountInfo, quantity: BigInteger) {
    user.inventory[1].free -= quantity
    user.inventory[1].locked += quantity
}

fun updateInventoryForNormalSeller(user: AccountInfo, quantity: BigInteger) {
    user.inventory[0].free -= quantity
    user.inventory[0].locked += quantity
}

fun updateWalletForBuyer(user: AccountInfo, quantity: BigInteger, price: BigInteger) {
    user.wallet.free -= quantity * price
    user.wallet.locked += quantity * price
}


fun validateBuyOrder(order: Order): Collection<String> {
    val errors = mutableListOf<String>()
    if (order.type == OrderType.BUY && order.esopType != null) {
        errors.add("BUY order cannot have a esop type (NORMAL / PERFORMANCE) specified")
    }
    errors.addAll(validateQuantity(order.quantity))
    errors.addAll(validatePrice(order.price))

    return errors
}

fun validateSellOrder(order: Order): Collection<String> {
    val errors = mutableListOf<String>()
    if (isEsopTypeSpecifiedForASellOrder(order)) {
        errors.add("Invalid field name or value for 'esopType' field")
        errors.add("Add a valid input for 'esopType' field")
    }
    errors.addAll(validateQuantity(order.quantity))
    errors.addAll(validatePrice(order.price))

    return errors
}

private fun isEsopTypeSpecifiedForASellOrder(order: Order) =
    order.type == OrderType.SELL && (order.esopType != EsopType.PERFORMANCE && order.esopType != EsopType.NORMAL)

fun validateQuantity(quantity: BigInteger): Collection<String> {
    if (quantity <= BigInteger.valueOf(0)) {
        return mutableListOf(
            "Invalid field name or value for the field 'quantity'",
            "Quantity must be positive integers"
        )
    }

    return mutableListOf()
}

fun validatePrice(price: BigInteger): Collection<String> {
    if (price <= BigInteger.valueOf(0)) {
        return mutableListOf(
            "Invalid field name or value for the field 'price'",
            "Price must be positive integers"
        )
    }

    return mutableListOf()
}