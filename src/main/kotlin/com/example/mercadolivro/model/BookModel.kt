package com.example.mercadolivro.model

import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.enums.Errors
import com.example.mercadolivro.exception.BadRequestException
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "BOOK")
data class BookModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    var customer: CustomerModel? = null
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if (field == BookStatus.CANCELED || field == BookStatus.DELETED)
                throw BadRequestException(Errors.ML102.message.format(field), Errors.ML102.code)

            field = value
        }

    constructor(
        id: Int? = null,
        name: String,
        price: BigDecimal,
        customer: CustomerModel? = null,
        status: BookStatus?
    ) : this(id, name, price, customer) {
        this.status = status
    }
}