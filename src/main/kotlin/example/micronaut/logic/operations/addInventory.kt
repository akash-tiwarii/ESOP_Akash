package com.example.logic.operations

import com.example.errors.ErrorMsgs
import com.example.logic.checks.checkUserPresence
import com.example.model.AddInventory
import com.example.model.Message
import java.lang.Exception
import java.math.BigInteger

fun validateInventory(inventoryObject: AddInventory, userName:String):Any
{
    val errorObject= ErrorMsgs(mutableListOf())
    if(!checkUserPresence(userName))
    {
        errorObject.error.add("User not registered")
        return errorObject
    }
    val qnt: BigInteger
    val type=inventoryObject.type

    try{
        qnt=inventoryObject.quantity.toBigInteger()
    }
    catch(e:Exception){
        errorObject.error.add("Invalid field name or value for the field 'quantity'")
        errorObject.error.add("Inventory should be a positive Integer not exceeding 9223372036854775806")
        return errorObject
    }

    if(!( qnt> BigInteger("0")  && qnt<=BigInteger("9223372036854775806")))
    {
        errorObject.error.add("Inventory should be a positive Integer not exceeding 9223372036854775806")
    }

    if(type=="" || (type!="NORMAL" && type!="PERFORMANCE"))
    {
        errorObject.error.add("Invalid value or field name for 'type' ")
    }

    if(errorObject.error.size>0)
        return errorObject

    addInventory(userName,type,qnt)

    return Message("$qnt ${inventoryObject.type} ESOPs added to account")

}

fun addInventory(userName:String,type:String,quantity:BigInteger)
{
    for(user in usersArray)
    {
        if(user.userName==userName)
        {
            if(type=="NORMAL")
                user.inventory[0].free+=quantity
            else if(type=="PERFORMANCE")
                user.inventory[1].free+=quantity
            
            break

        }
    }
}