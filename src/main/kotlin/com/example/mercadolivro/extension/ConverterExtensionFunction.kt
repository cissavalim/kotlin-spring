package com.example.mercadolivro.extension

import com.example.mercadolivro.controller.request.PostBookRequest
import com.example.mercadolivro.controller.request.PostCustomerRequest
import com.example.mercadolivro.controller.request.PutBookRequest
import com.example.mercadolivro.controller.request.PutCustomerRequest
import com.example.mercadolivro.controller.response.BookResponse
import com.example.mercadolivro.controller.response.CustomerResponse
import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.model.BookModel
import com.example.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ACTIVE,
        password = this.password
    )
}

fun PutCustomerRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
    return CustomerModel(
        id = customer.id,
        name = this.name,
        email = this.email,
        status = customer.status,
        password = customer.password
    )
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

fun CustomerModel.toCustomerResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toBookResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}