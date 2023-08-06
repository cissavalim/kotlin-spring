package com.example.mercadolivro.extension

import com.example.mercadolivro.controller.request.PostCustomerRequest
import com.example.mercadolivro.controller.request.PutCustomerRequest
import com.example.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel() : CustomerModel {
    return CustomerModel(name = this.name, email = this.email)
}

fun PutCustomerRequest.toCustomerModel(id: String) : CustomerModel {
    return CustomerModel(id = id, name = this.name, email = this.email)
}