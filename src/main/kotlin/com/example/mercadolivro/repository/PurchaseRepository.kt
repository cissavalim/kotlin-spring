package com.example.mercadolivro.repository

import com.example.mercadolivro.model.PurchaseModel
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRepository : JpaRepository<PurchaseModel, Int> {
}
