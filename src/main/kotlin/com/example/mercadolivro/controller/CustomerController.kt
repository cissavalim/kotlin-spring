package com.example.mercadolivro.controller

import com.example.mercadolivro.controller.request.PostCustomerRequest
import com.example.mercadolivro.controller.request.PutCustomerRequest
import com.example.mercadolivro.controller.response.CustomerResponse
import com.example.mercadolivro.extension.toCustomerModel
import com.example.mercadolivro.extension.toCustomerResponse
import com.example.mercadolivro.security.UserCanOnlyAccessTheirOwnResouces
import com.example.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    val customerService : CustomerService
) {

    @GetMapping
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {
        return customerService.getAll(name).map {
            it.toCustomerResponse()
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResouces
    fun getCustomer(@PathVariable id: Int): CustomerResponse {
        return customerService.findById(id).toCustomerResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessTheirOwnResouces
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerToUpdate = customerService.findById(id)
        customerService.update(customer.toCustomerModel(customerToUpdate))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
    }
}