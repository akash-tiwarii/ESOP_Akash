package example.micronaut.model

import java.math.BigInteger
import javax.annotation.Generated


data class Esop(val transactions: MutableList<Transaction>) {
    companion object {
        private var currentId : BigInteger = 1.toBigInteger()
    }
    val esopId : BigInteger = currentId++
}