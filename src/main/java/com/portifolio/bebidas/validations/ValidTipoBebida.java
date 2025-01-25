package com.portifolio.bebidas.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TipoBebidaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTipoBebida {
    String message() default "Tipo de bebida inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
