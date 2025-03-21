package com.portifolio.bebidas.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class SecaoExcedeuLimiteDeCapacidadeException extends BebidasException{

    private final String detail;

    public SecaoExcedeuLimiteDeCapacidadeException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Erro ao inserir bebidas.");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/secao"));
        return pd;
    }
}
