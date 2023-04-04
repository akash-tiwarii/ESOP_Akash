package example.micronaut.logic.checks

import example.micronaut.errors.Error
import example.micronaut.logic.operations.addEsopsFromFreeToLocked
import example.micronaut.logic.operations.usersArray
import example.micronaut.model.EsopType
import example.micronaut.model.Order
import example.micronaut.model.OrderType
import java.math.BigInteger

fun hasSuccessfullyLockMoneyAndInventoryForValidOrder(order: Order, uname: String): Any {

    val error = Error(mutableListOf())

    val price = BigInteger("0")
    val qnt = BigInteger("0")

    if(order.type == OrderType.SELL) {
        error.messages.addAll(validateSellOrder(order))
    } else {
        error.messages.addAll(validateBuyOrder(order))
    }

    if (error.messages.size != 0) {
        return error
    }

    var userFound = false

    for (user in usersArray) {
        if (uname == user.userName) {
            userFound = true
            if (price <= BigInteger("0")) {
                error.messages.add("Price must be positive integer")
            }
            if (qnt <= BigInteger("0")) {
                error.messages.add(" Quantity must be positive integer")
            }
            if (order.type == OrderType.BUY) {
                if (price > BigInteger("0") && qnt > BigInteger("0") && qnt * price <= user.wallet.free) {
                    user.wallet.free -= qnt * price
                    user.wallet.locked += qnt * price
                    return true
                } else {
                    error.messages.add("Insufficient balance")
                }
            } else if (order.type == OrderType.SELL) {
                // PERFORMANCE_type==NORMAL
                if (order.esopType == EsopType.NORMAL) {
                    if (qnt > user.inventory[0].free)
                        error.messages.add("Insufficient ESOPs in inventory")

                    if (price > BigInteger("0") && qnt > BigInteger("0") && qnt <= user.inventory[0].free) {
                        user.inventory[0].free -= qnt
                        addEsopsFromFreeToLocked(user, qnt, 0)
                        user.inventory[0].locked += qnt

                        return true
                    }
                }
                // PERFORMANCE_type==PERFORMANCE
                else {
                    if (qnt > user.inventory[1].free)
                        error.messages.add("Insufficient ESOPs in inventory")

                    if (price > BigInteger("0") && qnt > BigInteger("0") && qnt <= user.inventory[1].free) {
                        user.inventory[1].free -= qnt
                        addEsopsFromFreeToLocked(user, qnt, 1)
                        user.inventory[1].locked += qnt
                        return true
                    }

                }
            } else {
                error.messages.add("Type must be either BUY or SELL only")
            }
        }

    }

    if (!userFound) {
        error.messages.clear()
        error.messages.add("User not registered")

    }
    return error

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
    if (order.type == OrderType.SELL && (order.esopType != EsopType.PERFORMANCE && order.esopType != EsopType.NORMAL)) {
        errors.add("Invalid field name or value for 'esopType' field")
        errors.add("Add a valid input for 'esopType' field")
    }

    errors.addAll(validateQuantity(order.quantity))
    errors.addAll(validatePrice(order.price))

    return errors
}

fun validateQuantity(quantity: BigInteger): Collection<String> {
    if (quantity <= BigInteger("0")){
        return mutableListOf(
            "Invalid field name or value for the field 'quantity'",
            "Quantity must be positive integers"
        )
    }

    return mutableListOf()
}

fun validatePrice(price: BigInteger): Collection<String> {
    if (price <= BigInteger("0")){
        return mutableListOf(
            "Invalid field name or value for the field 'price'",
            "Price must be positive integers"
        )
    }

    return mutableListOf()
}