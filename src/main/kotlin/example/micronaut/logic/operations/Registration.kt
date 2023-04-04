package example.micronaut.logic.operations

import example.micronaut.errors.Error
import example.micronaut.exception.ApplicationException
import example.micronaut.logic.checks.*
import example.micronaut.model.*
import java.math.BigInteger

var usersArray = mutableListOf<AccountInfo>()

fun registerUser(reg: Register): Message {
    val errorObject = Error(mutableListOf())

    if (!checkPhone(reg.phoneNumber)) {
        errorObject.messages.add("Invalid field name or value for 'phoneNumber'")
    }
    if (!checkEmail(reg.email)) {
        errorObject.messages.add("Invalid field name or value for 'email'")
    }
    if (!checkFirstname(reg.firstName)) {
        errorObject.messages.add(("Invalid field name or value for 'firstName'"))
    }
    if (!checkLastname(reg.lastName)) {
        errorObject.messages.add(("Invalid field name or value for 'lastName'"))
    }
    if (!checkUsername(reg.username)) {
        errorObject.messages.add(("Invalid field name or value for 'username'"))
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

    if (checkUser(ob).size == 0 && errorObject.messages.size == 0) {
        usersArray.add(ob)
        return Message("Customer registered successfully")
    }


    for (uniqueError in checkUser(ob)) {
        if (uniqueError == 1)
            errorObject.messages.add("Customer phone number is already present")
        if (uniqueError == 2)
            errorObject.messages.add("Customer email is already present")
        if (uniqueError == 3)
            errorObject.messages.add("Customer user name is already present")
    }

    throw ApplicationException(errorObject.messages.joinToString(separator = ","))

}
