package com.example.mercadolivro.validation

import com.example.mercadolivro.service.CustomerService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailAvailableValidator(private val customerService: CustomerService) :
    ConstraintValidator<EmailAvailable, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value.isNullOrEmpty()) false
        else customerService.isEmailAvailable(value)
    }

}
