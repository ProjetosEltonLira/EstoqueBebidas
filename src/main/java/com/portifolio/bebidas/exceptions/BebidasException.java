package com.portifolio.bebidas.exceptions;

import org.springframework.http.ProblemDetail;

public class BebidasException extends RuntimeException {

    public BebidasException(String message){
        super(message);
    }

    public BebidasException(Throwable cause){
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(500);
        pd.setTitle("Estoque Bebidas Internal Server Error");
        pd.setDetail("Contact software support");
        return pd;
    }

}
