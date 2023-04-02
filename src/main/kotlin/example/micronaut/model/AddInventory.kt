package example.micronaut.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class AddInventory(
    var type: String = "",
    var quantity: String = "",

)