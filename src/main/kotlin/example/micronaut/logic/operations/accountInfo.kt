package example.micronaut.logic.operations

import example.micronaut.exception.ApplicationException
import example.micronaut.model.AccountInfo


fun getAccountInfo(userName: String): AccountInfo {
    for (user in usersArray) {
        if (user.userName == userName)
            return user
    }
    throw ApplicationException("User not registered")

}