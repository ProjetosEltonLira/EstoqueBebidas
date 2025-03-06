package com.portifolio.bebidas.entrypoint.validations;

import com.portifolio.bebidas.core.enums.TipoRegistro;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoRegistroValidator implements ConstraintValidator<ValidTipoRegistro, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }

        boolean isValid = TipoRegistro.getDescricoesPermitidas().contains(value.toUpperCase());

        if (!isValid) {
            // Gera a mensagem de erro dinâmica
            String mensagem = "Tipo de registro inválido. Valores permitidos: " +
                    TipoRegistro.getDescricoesPermitidas();

            // Configura a mensagem personalizada no contexto do validador
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(mensagem)
                    .addConstraintViolation();
        }
        return isValid;
    }
}