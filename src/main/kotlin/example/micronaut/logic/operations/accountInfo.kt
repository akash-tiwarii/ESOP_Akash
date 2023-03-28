package com.example.logic.operations
import com.example.errors.ErrorMsgs
import com.example.logic.checks.checkUserPresence


fun getAccountInfo(userName:String):Any{
    return if(checkUserPresence(userName))
        getUserInfo(userName)
    else
    {
        val errorObject=ErrorMsgs(mutableListOf())
        errorObject.error.add("User not registered")
        return errorObject
    }

}

fun getUserInfo(userName:String):Any
{
    for(user in usersArray)
    {
        if(user.userName==userName)
            return user
    }
    return false
}