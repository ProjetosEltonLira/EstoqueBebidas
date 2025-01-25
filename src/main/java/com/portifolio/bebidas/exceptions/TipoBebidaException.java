package com.portifolio.bebidas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class TipoBebidaException extends BebidasException{

    private final String detail;

    public TipoBebidaException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Tipo bebida invalida");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/secao"));
        return pd;
    }
}
