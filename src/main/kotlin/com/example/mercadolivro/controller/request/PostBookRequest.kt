package com.example.mercadolivro.controller.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PostBookRequest(
    var name: String,
    var price: BigDecimal,
    var customerId: Int
)