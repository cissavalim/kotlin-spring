package com.example.mercadolivro.validation

import com.example.mercadolivro.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailAvailableValidator(val customerService: CustomerService) : ConstraintValidator<EmailAvailable, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value.isNullOrEmpty()) false
        else customerService.isEmailAvailable(value)
    }

}
