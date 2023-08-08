package com.example.mercadolivro.controller.response

import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.model.CustomerModel
import java.math.BigDecimal

class BookResponse (
    var id: Int? = null,
    var name: String,
    var price: BigDecimal,
    var customer: CustomerModel? = null,
    var status: BookStatus? = null
)
