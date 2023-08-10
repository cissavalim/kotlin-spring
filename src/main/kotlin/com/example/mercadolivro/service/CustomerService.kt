package com.example.mercadolivro.service

import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.enums.Errors
import com.example.mercadolivro.exception.NotFoundException
import com.example.mercadolivro.model.CustomerModel
import com.example.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    var bookService: BookService
) {

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()
    }

    fun create(customer: CustomerModel) {
        customerRepository.save(customer)
    }

    fun findById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow() { NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
    }

    fun update(customer: CustomerModel) {
        customerRepository.findById(customer.id!!).orElseThrow()
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        val customer = findById(id)
        bookService.deleteByCustomer(customer)

        customer.status = CustomerStatus.INACTIVE

        customerRepository.save(customer)
    }

    fun isEmailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }
}