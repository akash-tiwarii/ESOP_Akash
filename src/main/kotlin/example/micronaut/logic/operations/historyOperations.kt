package com.example.logic.operations

import com.example.errors.ErrorMsgs
import com.example.logic.checks.checkUserPresence
import com.example.model.OrderFilled
import com.example.model.OrderResponseSell

fun getHistoryOf(username:String):Any{

    if(!checkUserPresence(username))
    {
        val errorObject=ErrorMsgs(mutableListOf())
        errorObject.error.add("User not registered")
        return errorObject
    }

    var historyList= mutableListOf<OrderFilled>()
    for(orderFilled in orderMap.keys)
    {
        if(orderMap[orderFilled]==username) {
            historyList.add(orderFilled)
        }
    }

    historyList = historyList.sortedWith(compareByDescending { it.orderId }).toMutableList()

    return historyList
}