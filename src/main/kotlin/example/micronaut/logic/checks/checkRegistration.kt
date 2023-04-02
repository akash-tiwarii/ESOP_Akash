package example.micronaut.logic.checks

import example.micronaut.logic.operations.usersArray

import example.micronaut.model.AccountInfo


fun checkPhone(phoneNo: String): Boolean {
    return phoneNo.isNotBlank() && phoneNo.isNotEmpty() && phoneNo.all { it.isDigit() } && phoneNo.length == 10 && phoneNo[0] != '0'
}

fun checkEmail(mail: String): Boolean {
    //val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    val emailRegex =
        "\\A[a-z0-9!#$%&'*+/=?^_‘{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_‘{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z"
    return mail.isNotBlank() && mail.isNotEmpty() && mail.matches(emailRegex.toRegex())
}

fun checkUser(ob: AccountInfo): MutableList<Int> {
    val uniqueErrors = mutableListOf<Int>()
    for (user in usersArray) {
        if (ob.phoneNumber == user.phoneNumber)
            uniqueErrors.add(1)
        if (ob.email == user.email)
            uniqueErrors.add(2)
        if (ob.userName == user.userName)
            uniqueErrors.add(3)
    }
    return uniqueErrors//unique user
}

fun checkFirstname(firstName: String): Boolean {
    return firstName.isNotEmpty() && firstName.isNotBlank() && firstName.all { it.isLetter() }
}

fun checkLastname(lastName: String): Boolean {
    return lastName.isNotEmpty() && lastName.isNotBlank() && lastName.all { it.isLetter() }
}

fun checkUsername(userName: String): Boolean {
    val usernameRegex = "^[A-Za-z0-9_]*$"
    return userName.isNotEmpty() && userName.isNotBlank() && userName.matches(usernameRegex.toRegex())
}