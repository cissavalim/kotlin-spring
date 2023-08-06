package com.example.mercadolivro.repository

import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.model.BookModel
import com.example.mercadolivro.model.CustomerModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<BookModel, Int> {
    fun findByStatus(status: BookStatus): List<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}