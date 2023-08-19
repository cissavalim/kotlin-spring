package com.example.mercadolivro.security

import com.example.mercadolivro.controller.request.LoginRequest
import com.example.mercadolivro.exception.AuthenticationException
import com.example.mercadolivro.repository.CustomerRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val customerRepository : CustomerRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try{
            val loginRequest = jacksonObjectMapper().readValue(request.inputStream, LoginRequest::class.java)
            val id = customerRepository.findByEmail(loginRequest.email).id
            val authToken = UsernamePasswordAuthenticationToken(id, loginRequest.password)

            return authenticationManager.authenticate(authToken)
        } catch (ex: Exception) {
            throw AuthenticationException("Falha ao autenticar", "999")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val id = (authResult.principal as UserCustomDetails).username
        val token = jwtUtil.generateToken(id)
        response.addHeader("Authorization", "Bearer $token")
    }
}