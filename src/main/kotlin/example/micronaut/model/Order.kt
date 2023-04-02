package example.micronaut.model

data class Order(
    var quantity: String,
    var type: String,
    var esopType: String = "",
    var price: String
)