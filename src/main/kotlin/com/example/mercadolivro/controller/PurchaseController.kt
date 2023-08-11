package com.example.mercadolivro.controller

import com.example.mercadolivro.controller.mapper.PurchaseMapper
import com.example.mercadolivro.controller.request.PostPurchaseRequest
import com.example.mercadolivro.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController("/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: PostPurchaseRequest) {
        purchaseService.create(purchaseMapper.toModel(request))
    }
}