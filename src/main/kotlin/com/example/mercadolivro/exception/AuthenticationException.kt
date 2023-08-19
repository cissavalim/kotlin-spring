package com.example.mercadolivro.exception

class AuthenticationException(override val message: String, val errorCode: String) : Exception()