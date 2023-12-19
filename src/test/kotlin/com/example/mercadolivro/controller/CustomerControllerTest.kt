package com.example.mercadolivro.controller

import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.enums.Role
import com.example.mercadolivro.exception.NotFoundException
import com.example.mercadolivro.model.CustomerModel
import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.service.BookService
import com.example.mercadolivro.service.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        val name = UUID.randomUUID().toString()
        val customersList = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findByNameContaining(name) } returns customersList

        val customers = customerService.getAll(name)

        assertEquals(customersList, customers)
        verify(exactly = 0) { customerRepository.findAll() }
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = Random().nextInt().toString()
        val customer = buildCustomer(password = initialPassword)
        val encryptedPassword = UUID.randomUUID().toString()
        val customerEncrypted = customer.copy(password = encryptedPassword)

        every { customerRepository.save(customerEncrypted) } returns customer
        every { bCrypt.encode(initialPassword) } returns encryptedPassword

        customerService.create(customer)

        verify(exactly = 1) { customerRepository.save(customerEncrypted) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(customer)

        val customerReturned = customerService.findById(id)

        assertEquals(customer, customerReturned)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw when customer is not found`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.findById(id) }

        assertEquals("Customer $id not found", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(customer)
        every { customerRepository.save(customer) } returns customer

        customerService.update(customer)

        verify(exactly = 1) { customerRepository.findById(id) }
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `should throw when customer is not found on update`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.update(customer) }

        assertEquals("Customer $id not found", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)
        val expectedCustomer = customer.copy(status = CustomerStatus.INACTIVE)

        every { customerRepository.findById(id) } returns Optional.of(customer)
        every { bookService.deleteByCustomer(customer) } just runs
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer

        customerService.delete(id)

        verify(exactly = 1) { bookService.deleteByCustomer(customer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw when customer is not found on delete`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.delete(id) }

        assertEquals("Customer $id not found", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email is available`() {
        val email = "${Random().nextInt()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.isEmailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should return false when email is unavailable`() {
        val email = "${Random().nextInt()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.isEmailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
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