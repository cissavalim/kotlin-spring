package com.example.mercadolivro.security

import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    private val customerModel: CustomerModel
) : UserDetails {

    val id = username.toInt()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = customerModel.role.map {
        SimpleGrantedAuthority(it.description)
    }.toMutableList()

    override fun getPassword(): String = customerModel.password

    override fun getUsername(): String = customerModel.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = customerModel.status == CustomerStatus.ACTIVE

}