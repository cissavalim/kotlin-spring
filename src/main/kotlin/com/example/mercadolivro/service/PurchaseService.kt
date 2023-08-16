package com.example.mercadolivro.service

import com.example.mercadolivro.enums.BookStatus
import com.example.mercadolivro.enums.Errors
import com.example.mercadolivro.events.PurchaseEvent
import com.example.mercadolivro.exception.BadRequestException
import com.example.mercadolivro.model.PurchaseModel
import com.example.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchaseModel: PurchaseModel) {
        val invalidBooks = purchaseModel.books.filter { it.status != BookStatus.ACTIVE }

        if(invalidBooks.isEmpty()) purchaseRepository.save(purchaseModel)
        else throw BadRequestException(Errors.ML103.message.format(invalidBooks.map { it.id }, invalidBooks.map { it.status }), Errors.ML103.code)

        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

}
