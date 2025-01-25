package com.portifolio.bebidas.exceptions;

import com.portifolio.bebidas.exceptions.dto.ParamentrosInvalidosDto;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BebidasException.class)
    public ProblemDetail handleJBankException(BebidasException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var invalidParams = e.getFieldErrors()
                .stream()
                .map(fieldError -> new ParamentrosInvalidosDto(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("Parametros da requisicao invalidos");
        pd.setDetail("Ha campos invalidos na requisicao");
        pd.setProperty("Parametros-invalidos", invalidParams);

        return pd;
    }
}
