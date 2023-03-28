package com.example.logic.operations

import com.example.errors.ErrorMsgs
import com.example.logic.checks.checkUserPresence
import com.example.model.AddWallet
import com.example.model.Message
import java.lang.Exception
import java.math.BigInteger

fun validateWallet(walletObject:AddWallet,userName:String):Any{
    val errorObject = ErrorMsgs(mutableListOf())
    if(!checkUserPresence(userName))
    {
        errorObject.error.add("User not registered")
        return errorObject
    }
    val amt: BigInteger
    try {
       amt = walletObject.amount.toBigInteger()
    }
    catch(e:Exception){
        errorObject.error.add("Invalid field name or value for the field 'amount' ")
        errorObject.error.add("Amount should be a positive Integer not exceeding 9223372036854775806")
        return errorObject
    }
    if(!(amt>BigInteger("0") && amt<=BigInteger("9223372036854775806")))
    {
        errorObject.error.add("Amount should be a positive Integer not exceeding 9223372036854775806")
        return errorObject
    }

    addWallet(userName,amt)

    return Message("$amt added to account")
}

fun addWallet(userName: String,amt:BigInteger)
{
    for(user in usersArray)
    {
        if(user.userName==userName)
            user.wallet.free+=amt
    }
}