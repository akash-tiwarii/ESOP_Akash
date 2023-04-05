package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.model.AccountInfo
import java.lang.Exception


fun getAccountInfo(userName: String): AccountInfo {
    for (user in usersArray) {
        if (user.userName == userName)
            return user
    }

    val errorObject = Error(mutableListOf())
    throw Exception("User not registered")
//    errorObject.messages.add("User not registered")
//    throw ApplicationException(errorObject.messages.joinToString(separator = ","))
}