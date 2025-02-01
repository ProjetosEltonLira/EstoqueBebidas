package com.portifolio.bebidas.utils;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

public class HeaderUtil {

    public static HttpHeaders getCorrelationId() {

        String correlationId = MDC.get("X-Correlation-ID");
        HttpHeaders headers = new HttpHeaders();
        if (correlationId != null) {
            headers.set("X-Correlation-ID", correlationId);
        }
        return headers;
    }
}