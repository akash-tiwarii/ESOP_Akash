package example.micronaut.logic.operations

import java.math.BigInteger


private var totalTransactionFee: BigInteger = 0.toBigInteger()
fun addTransactionFeeToOrganization(fee: BigInteger): BigInteger {
    totalTransactionFee += fee
    return totalTransactionFee
}
fun getTransactionFeeToOrganization(): BigInteger {
    return totalTransactionFee
}
