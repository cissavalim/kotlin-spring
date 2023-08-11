package com.example.mercadolivro.controller.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PostBookRequest(

    @field:NotEmpty(message = "Name must not be null or empty")
    var name: String,

    @field:NotNull(message = "Price must not be null or empty")
    var price: BigDecimal,

    var customerId: Int
)