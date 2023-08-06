package com.example.mercadolivro.model

import jakarta.persistence.*

@Entity(name = "CUSTOMER")
data class CustomerModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String
)