package com.example.mercadolivro.extension

import com.example.mercadolivro.controller.request.PostBookRequest
import com.example.mercadolivro.controller.request.PostCustomerRequest
import com.example.mercadolivro.controller.request.PutBookRequest
import com.example.mercadolivro.controller.request.PutCustomerRequest
import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.model.BookModel
import com.example.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name, email = this.email)
}

fun PutCustomerRequest.toCustomerModel(id: Int): CustomerModel {
    return CustomerModel(id = id, name = this.name, email = this.email)
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customer
    )
}

fun PutBookRequest.toBookModel(book: BookModel): BookModel {
    return BookModel(
        id = book.id,
        name = this.name ?: book.name,
        price = this.price ?: book.price,
        status = book.status,
        customer = book.customer
    )
}