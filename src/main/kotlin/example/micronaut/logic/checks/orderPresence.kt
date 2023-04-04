package example.micronaut.logic.checks

import example.micronaut.logic.operations.mappedOrders

fun checkOrderPresence(orderId: String) {
    for(orderResponse in mappedOrders.keys){
        if(orderId == orderResponse.orderId){
            // Updating update or delete
        }
        
    }
}