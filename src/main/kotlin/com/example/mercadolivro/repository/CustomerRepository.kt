package com.example.mercadolivro.repository

import com.example.mercadolivro.model.CustomerModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<CustomerModel, Int> {

    fun findByNameContaining(name: String) : List<CustomerModel>
    fun existsByEmail(email: String) : Boolean
    fun findByEmail(email: String) : CustomerModel
}