package com.example.mercadolivro.config

import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.security.AuthenticationFilter
import com.example.mercadolivro.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer

class MyCustomDsl(
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
): AbstractHttpConfigurer<MyCustomDsl, HttpSecurity>() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val authenticationManager = http.getSharedObject(
            AuthenticationManager::class.java
        )
        http.addFilter(AuthenticationFilter(customerRepository, authenticationManager, jwtUtil))
    }

    fun customDsl(): MyCustomDsl {
        return MyCustomDsl(customerRepository, jwtUtil)
    }
}