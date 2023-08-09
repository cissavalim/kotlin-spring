package com.example.mercadolivro.exception

class NotFoundException(override val message: String, val errorCode: String) : Exception() {
}