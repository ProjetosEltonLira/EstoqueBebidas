package com.portifolio.bebidas.validations;

import com.portifolio.bebidas.enums.TipoBebida;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoBebidaValidator implements ConstraintValidator<ValidTipoBebida, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        // Verifica se o valor está na lista de descrições permitidas
        boolean isValid = TipoBebida.getDescricoesPermitidas().contains(value.toUpperCase());

        if (!isValid) {
            // Gera a mensagem de erro dinâmica
            String mensagem = "Tipo de bebida inválida. Valores permitidos: " +
                    TipoBebida.getDescricoesPermitidas();

            // Configura a mensagem personalizada no contexto do validador
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(mensagem)
                    .addConstraintViolation();
        }
        return isValid;
    }
}