package com.portifolio.bebidas.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TipoRegistroValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTipoRegistro {
    String message() default "Tipo de registro inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
