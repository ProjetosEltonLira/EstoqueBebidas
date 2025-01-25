package com.portifolio.bebidas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class SecaoException extends BebidasException{

    private final String detail;

    public SecaoException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Secao invalida");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/secao"));
        return pd;
    }
}
