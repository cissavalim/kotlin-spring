package com.example.mercadolivro.service

import com.example.mercadolivro.exception.AuthenticationException
import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt()).orElseThrow { AuthenticationException("Usuario nao encontrado", "999") }
        return UserCustomDetails(customer)
    }
}