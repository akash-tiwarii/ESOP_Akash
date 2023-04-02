package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.checkUserPresence
import example.micronaut.model.OrderFilled

fun getHistoryOf(username: String): List<OrderFilled> {

    if (!checkUserPresence(username)) {
        val errorObject = ErrorMsgs(mutableListOf())
        errorObject.error.add("User not registered")
        throw ApplicationException(errorObject.error.joinToString(separator = ","))
    }

    var historyList = mutableListOf<OrderFilled>()
    for (orderFilled in orderMap.keys) {
        if (orderMap[orderFilled] == username) {
            historyList.add(orderFilled)
        }
    }

    historyList = historyList.sortedWith(compareByDescending { it.orderId }).toMutableList()

    return historyList
}