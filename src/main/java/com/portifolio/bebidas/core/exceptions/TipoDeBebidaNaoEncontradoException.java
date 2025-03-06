package com.portifolio.bebidas.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class TipoDeBebidaNaoEncontradoException extends BebidasException{

    private final String detail;

    public TipoDeBebidaNaoEncontradoException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Erro ao inserir bebidas.");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/secao/"));
        return pd;
    }
}
