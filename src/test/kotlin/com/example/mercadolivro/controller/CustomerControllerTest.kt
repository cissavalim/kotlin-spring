package com.example.mercadolivro.controller

import com.example.mercadolivro.controller.request.PostCustomerRequest
import com.example.mercadolivro.controller.request.PutCustomerRequest
import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.enums.Errors
import com.example.mercadolivro.helper.buildCustomer
import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.security.UserCustomDetails
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mapper: ObjectMapper

    companion object {
        private const val CUSTOMERS_ENDPOINT: String = "/customers"
    }

    @BeforeEach
    fun setUp() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customers`() {
        val customer1 = customerRepository.save(buildCustomer())
        val customer2 = customerRepository.save(buildCustomer())

        mockMvc.perform(
                get(CUSTOMERS_ENDPOINT)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].id").value(customer1.id))
                .andExpect(jsonPath("$[0].name").value(customer1.name))
                .andExpect(jsonPath("$[0].email").value(customer1.email))
                .andExpect(jsonPath("$[0].status").value(customer1.status.name))
                .andExpect(jsonPath("$[1].id").value(customer2.id))
                .andExpect(jsonPath("$[1].name").value(customer2.name))
                .andExpect(jsonPath("$[1].email").value(customer2.email))
                .andExpect(jsonPath("$[1].status").value(customer2.status.name))

    }

    @Test
    fun `should filter all customers by name`() {
        val customer1 = customerRepository.save(buildCustomer(name = "Cissa"))

        mockMvc.perform(
                get(CUSTOMERS_ENDPOINT)
                    .queryParam("name", "Cis")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(customer1.id))
                .andExpect(jsonPath("$[0].name").value(customer1.name))
                .andExpect(jsonPath("$[0].email").value(customer1.email))
                .andExpect(jsonPath("$[0].status").value(customer1.status.name))
    }

    @Test
    fun `should create customer`() {
        val request = PostCustomerRequest(
            "name",
            "${Random.nextInt()}@email.com",
            "123456"
        )

        mockMvc.perform(
            post(CUSTOMERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers.first().name)
        assertEquals(request.email, customers.first().email)
    }

    @Test
    fun `should throw error on create when customer has invalid information`() {
        val request = PostCustomerRequest(
            "",
            "${Random.nextInt()}@email.com",
            "123456"
        )

        mockMvc.perform(
            post(CUSTOMERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.message").value(Errors.ML001.message))
            .andExpect(jsonPath("$.internalCode").value(Errors.ML001.code))
    }

    @Test
    fun `should get user by id when user has the same id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
                get("$CUSTOMERS_ENDPOINT/${customer.id}")
                    .with(user(UserCustomDetails(customer)))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))

    }

    @Test
    fun `should return forbidden when user has not the same id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
            get("$CUSTOMERS_ENDPOINT/0")
                .with(user(UserCustomDetails(customer)))
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.httpCode").value(HttpStatus.FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(Errors.ML000.message))
            .andExpect(jsonPath("$.internalCode").value(Errors.ML000.code))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get user by id when user is admin`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
            get("$CUSTOMERS_ENDPOINT/${customer.id}")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))
    }

    @Test
    fun `should update user`() {
        val customer = customerRepository.save(buildCustomer())
        val customerUpdateRequest = PutCustomerRequest("Jose", "jose@gmail.com")

        mockMvc.perform(
            put("$CUSTOMERS_ENDPOINT/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerUpdateRequest))
                .with(user(UserCustomDetails(customer)))
        )
            .andExpect(status().isNoContent)

        val customerRetrieved = customerRepository.findAll()
        assertEquals(1, customerRetrieved.size)
        assertEquals(customerUpdateRequest.name, customerRetrieved.first().name)
        assertEquals(customerUpdateRequest.email, customerRetrieved.first().email)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return not found on update nonexistent user`() {
        val customerUpdateRequest = PutCustomerRequest("Jose", "jose@gmail.com")

        mockMvc.perform(
            put("$CUSTOMERS_ENDPOINT/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerUpdateRequest))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(String.format(Errors.ML201.message, "1")))
            .andExpect(jsonPath("$.internalCode").value(Errors.ML201.code))
    }

    @Test
    fun `should throw error when update customer has invalid information`() {
        val request = PutCustomerRequest(
            "",
            "${Random.nextInt()}@email.com"
        )

        mockMvc.perform(
            put("$CUSTOMERS_ENDPOINT/${Random.nextInt()}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.message").value(Errors.ML001.message))
            .andExpect(jsonPath("$.internalCode").value(Errors.ML001.code))
    }

    @Test
    fun `should delete customer`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
            delete("$CUSTOMERS_ENDPOINT/${customer.id}")
        )
            .andExpect(status().isNoContent)

        val deleted = customerRepository.findById(customer.id!!)

        assertEquals(CustomerStatus.INACTIVE, deleted.get().status)
    }

    @Test
    fun `should return not found on delete nonexistent customer`() {
        mockMvc.perform(
            delete("$CUSTOMERS_ENDPOINT/1")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(String.format(Errors.ML201.message, "1")))
            .andExpect(jsonPath("$.internalCode").value(Errors.ML201.code))
    }
}