package com.example.mercadolivro.model

import com.example.mercadolivro.enums.CustomerStatus
import com.example.mercadolivro.enums.Role
import jakarta.persistence.*

@Entity(name = "CUSTOMER")
data class CustomerModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus,

    @Column
    var password: String,

    @CollectionTable(name = "CUSTOMER_ROLE", joinColumns = [JoinColumn(name = "customer_id")])
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @Column
    @Enumerated(EnumType.STRING)
    var role: Set<Role> = setOf()
)