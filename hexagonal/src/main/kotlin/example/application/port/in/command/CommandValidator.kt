package example.application.port.`in`.command

import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.Validator


abstract class CommandValidator<T>(private val validator: Validator = Validation.buildDefaultValidatorFactory().validator) {

    fun validate() {
        val violations = validator.validate(this as T)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }

}