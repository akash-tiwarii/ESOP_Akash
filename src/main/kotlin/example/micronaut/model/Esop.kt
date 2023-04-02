package example.micronaut.model

import java.math.BigInteger


data class Esop(val transactions: MutableList<Transaction>) {
    companion object {
        private var currentId: BigInteger = 1.toBigInteger()
    }

    val esopId: BigInteger = currentId++
}