package com.example.mercadolivro.model

import com.example.mercadolivro.enums.CustomerStatus
import javax.persistence.*

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
    var status: CustomerStatus
)