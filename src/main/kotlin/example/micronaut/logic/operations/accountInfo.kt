package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.model.AccountInfo


fun getAccountInfo(userName: String): AccountInfo {
    for (user in usersArray) {
        if (user.userName == userName)
            return user
    }


    val errorObject = ErrorMsgs(mutableListOf())
    errorObject.error.add("User not registered")
    throw ApplicationException(errorObject.error.joinToString(separator = ","))

}