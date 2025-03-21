package com.portifolio.bebidas.core.exceptions;

import com.portifolio.bebidas.core.exceptions.dto.ParamentrosInvalidosDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BebidasException.class)
    public ResponseEntity<ProblemDetail> handleBebidasException(BebidasException bebidasException) {
        var problemDetail = bebidasException.toProblemDetail();

        var responseEntity = ResponseEntity.status(problemDetail.getStatus())
                //.headers(HeaderUtil.getCorrelationId())  // Inclui o Correlation ID nos headers
                .body(problemDetail);
        logger.error("Exception: {}", responseEntity);

        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail>  handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var invalidParams = e.getFieldErrors()
                .stream()
                .map(fieldError -> new ParamentrosInvalidosDto(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var problemDetail = ProblemDetail.forStatus(UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Parametros da requisicao invalidos");
        problemDetail.setDetail("Ha campos invalidos na requisicao");
        problemDetail.setProperty("Parametros-invalidos", invalidParams);

        //Modifiquei a forma de retorno, para poder retornar o correlation id no Header da exception
        //  return pd;
        var responseEntity = ResponseEntity.status(problemDetail.getStatus())
                //.headers(HeaderUtil.getCorrelationId())
                .body(problemDetail);

        logger.error("Error de validação : {}",responseEntity);
        return responseEntity;
    }

//    @ExceptionHandler(HeaderObrigatoriosException.class)
//    public ResponseEntity<Map<String, String>> handleMissingHeadersException(HeaderObrigatoriosException ex) {
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("Erro", "Bad Request");
//        errorResponse.put("Mensagem", ex.getMessage());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
}
