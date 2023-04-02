package example.micronaut.logic.operations

import example.micronaut.errors.ErrorMsgs
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.*
import example.micronaut.model.*
import java.math.BigInteger

var usersArray = mutableListOf<AccountInfo>()

fun registerUser(reg: Register): Message {
    val errorObject = ErrorMsgs(mutableListOf())

    if (!checkPhone(reg.phoneNumber)) {
        errorObject.error.add("Invalid field name or value for 'phoneNumber'")
    }
    if (!checkEmail(reg.email)) {
        errorObject.error.add("Invalid field name or value for 'email'")
    }
    if (!checkFirstname(reg.firstName)) {
        errorObject.error.add(("Invalid field name or value for 'firstName'"))
    }
    if (!checkLastname(reg.lastName)) {
        errorObject.error.add(("Invalid field name or value for 'lastName'"))
    }
    if (!checkUsername(reg.username)) {
        errorObject.error.add(("Invalid field name or value for 'username'"))
    }
    //return errorObject


    val newInventory = ArrayList<Inventory>()
    newInventory.add(Inventory("NORMAL", BigInteger("0"), BigInteger("0")))
    newInventory.add(Inventory("PERFORMANCE", BigInteger("0"), BigInteger("0")))


    val ob = AccountInfo(
        reg.firstName,
        reg.lastName,
        reg.phoneNumber,
        reg.username,
        reg.email,
        Wallet(BigInteger("0"), BigInteger("0")),
        newInventory
    )

    if (checkUser(ob).size == 0 && errorObject.error.size == 0) {
        usersArray.add(ob)
        return Message("Customer registered successfully")
    }


    for (uniqueError in checkUser(ob)) {
        if (uniqueError == 1)
            errorObject.error.add("Customer phone number is already present")
        if (uniqueError == 2)
            errorObject.error.add("Customer email is already present")
        if (uniqueError == 3)
            errorObject.error.add("Customer user name is already present")
    }

    throw ApplicationException(errorObject.error.joinToString(separator = ","))

}
