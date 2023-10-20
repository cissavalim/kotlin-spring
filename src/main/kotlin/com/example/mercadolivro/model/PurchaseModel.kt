package com.example.mercadolivro.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "PURCHASE")
data class PurchaseModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel,

    @ManyToMany
    @JoinTable(
        name = "PURCHASE_BOOK",
        joinColumns = [JoinColumn(name = "PURCHASE_ID")],
        inverseJoinColumns = [JoinColumn(name = "BOOK_ID")]
    )
    var books: List<BookModel>,

    @Column
    val nfe: String? = null,

    @Column
    val price: BigDecimal,

    @Column(name = "CREATED_AT")
    val createdAt: LocalDateTime = LocalDateTime.now()
)