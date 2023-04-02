package example.micronaut.logic.checks

import example.micronaut.logic.operations.usersArray

fun checkUserPresence(userName: String): Boolean {
    for (user in usersArray) {
        if (user.userName == userName) {
            return true
        }
    }
    return false
}