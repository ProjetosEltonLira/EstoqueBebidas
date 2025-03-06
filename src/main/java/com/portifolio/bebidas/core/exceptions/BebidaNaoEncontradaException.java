package com.portifolio.bebidas.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class BebidaNaoEncontradaException extends BebidasException{

    private final String detail;

    public BebidaNaoEncontradaException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Bebida não encontrada");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/bebida"));
        return pd;
    }
}
