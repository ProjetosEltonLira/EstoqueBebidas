package com.portifolio.bebidas.exceptions;

import com.portifolio.bebidas.exceptions.dto.ParamentrosInvalidosDto;
import com.portifolio.bebidas.utils.HeaderUtil;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BebidasException.class)
    public ResponseEntity<ProblemDetail> handleBebidasException(BebidasException bebidasException) {
        var problemDetail = bebidasException.toProblemDetail();

        return ResponseEntity.status(problemDetail.getStatus())
                .headers(HeaderUtil.getCorrelationId())  // Inclui o Correlation ID nos headers
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail>  handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var invalidParams = e.getFieldErrors()
                .stream()
                .map(fieldError -> new ParamentrosInvalidosDto(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var problemDetail = ProblemDetail.forStatus(422);

        problemDetail.setTitle("Parametros da requisicao invalidos");
        problemDetail.setDetail("Ha campos invalidos na requisicao");
        problemDetail.setProperty("Parametros-invalidos", invalidParams);

        //Modifiquei a forma de retorno, para poder retornar o correlation id no Header da exception
        //  return pd;
        return ResponseEntity.status(problemDetail.getStatus())
                .headers(HeaderUtil.getCorrelationId())
                .body(problemDetail);
    }
}
