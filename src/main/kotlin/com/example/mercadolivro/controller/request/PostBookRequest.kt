package com.example.mercadolivro.controller.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PostBookRequest(

    @field:NotEmpty(message = "Name must not be null or empty")
    var name: String,

    @field:NotNull(message = "Price must not be null or empty")
    var price: BigDecimal,

    var customerId: Int
)