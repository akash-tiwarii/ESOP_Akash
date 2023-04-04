package example.micronaut.model

data class AddInventory(
    var type: EsopType,
    var quantity: String = "",

    )