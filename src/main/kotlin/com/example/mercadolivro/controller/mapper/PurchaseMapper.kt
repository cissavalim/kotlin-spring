package com.example.mercadolivro.controller.mapper

import com.example.mercadolivro.controller.request.PostPurchaseRequest
import com.example.mercadolivro.model.PurchaseModel
import com.example.mercadolivro.service.BookService
import com.example.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toModel(request: PostPurchaseRequest) : PurchaseModel {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)

        return PurchaseModel(
            customer = customer,
            books = books,
            price = books.sumOf { it.price }
        )
    }
}