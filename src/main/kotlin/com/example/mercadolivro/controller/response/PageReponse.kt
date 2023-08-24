package com.example.mercadolivro.controller.response

class PageReponse<T>(
    var items: List<T>,
    val currentPage: Int,
    var totalItems: Long,
    val totalPages: Int
)