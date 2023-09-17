package example.application.port.`in`.command

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.context.ContextLoader
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.Validator


abstract class CommandValidator<T> {

    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    init {
        validate()
    }

    fun validate() {
        val violations = validator.validate(this as T)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }

}