package com.example.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest (

    @field:NotEmpty(message = "Name must not be null or empty")
    var name: String,

    @field:Email(message = "E-mail must be valid")
    var email: String
)