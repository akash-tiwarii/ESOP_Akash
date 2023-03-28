package com.example.logic.checks
import com.example.logic.operations.usersArray
fun checkUserPresence(userName:String):Boolean
{
    for(user in usersArray)
    {
        if(user.userName==userName) {
            return true
        }
    }
    return false
}