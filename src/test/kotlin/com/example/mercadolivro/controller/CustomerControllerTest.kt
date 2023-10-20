package com.example.mercadolivro.controller

import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.enums.Role
import com.example.mercadolivro.model.CustomerModel
import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.service.BookService
import com.example.mercadolivro.service.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerControllerTest {

    @InjectMockKs
    private lateinit var customerService: CustomerService

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @Test
    fun `should return all customers`() {
        val customer = buildCustomer()
        val customersList = listOf(customer)
        every { customerRepository.findAll() } returns customersList

        val customers = customerService.getAll(null)

        assertEquals(customersList, customers)
        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `should return when name is informed`() {
        val name = "Cissa"
        val customersList = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findByNameContaining(name) } returns customersList

        val customers = customerService.getAll(name)

        assertEquals(customersList, customers)
        verify(exactly = 0) { customerRepository.findAll() }
        verify(exactly = 1) { customerRepository.findByNameContaining(any()) }
    }

    private fun buildCustomer(
        id: Int? = null,
        name: String = "customer name",
        email: String = "${UUID.randomUUID()}@email.com",
        password: String = "password"
    ) = CustomerModel(
        id = id,
        name = name,
        email = email,
        status = CustomerStatus.ACTIVE,
        password = password,
        role = setOf(Role.CUSTOMER)
    )
}