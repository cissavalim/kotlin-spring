package com.example.mercadolivro.config

import com.example.mercadolivro.enums.Role
import com.example.mercadolivro.repository.CustomerRepository
import com.example.mercadolivro.security.AuthenticationFilter
import com.example.mercadolivro.security.AuthorizationFilter
import com.example.mercadolivro.security.CustomAuthenticationEntryPoint
import com.example.mercadolivro.security.JwtUtil
import com.example.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JwtUtil,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers"
    )

    private val PUBLIC_GET_MATCHERS = arrayOf(
        "/books"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )

    private val SWAGER_MATCHERS = arrayOf(
        "/v2/api-docs",
        "/v3/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/csrf/**"
    )

    fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                    .requestMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
                    .requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
                    .requestMatchers(HttpMethod.GET, "/customers").hasAuthority(Role.ADMIN.description)
                    .requestMatchers(*SWAGER_MATCHERS).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilter(AuthenticationFilter(customerRepository, authenticationManager(), jwtUtil))
            .addFilter(AuthorizationFilter(authenticationManager(), jwtUtil, userDetails))
            .exceptionHandling { it.authenticationEntryPoint(customAuthenticationEntryPoint) }

        return http.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfig(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}