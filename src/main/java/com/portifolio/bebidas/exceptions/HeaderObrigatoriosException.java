package com.portifolio.bebidas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class HeaderObrigatoriosException extends BebidasException{

    private final String detail;

    public HeaderObrigatoriosException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Headers invalidos invalidos");
        pd.setDetail(detail);
        pd.setInstance(URI.create("/*"));
        return pd;
    }
}
