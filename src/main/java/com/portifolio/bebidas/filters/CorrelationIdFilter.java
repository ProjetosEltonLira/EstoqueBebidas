package com.portifolio.bebidas.filters;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends HttpFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    protected void doFilter(HttpServletRequest httpRequest, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        // Adiciona o Correlation ID no MDC (usado pelo SLF4J para logs)
        MDC.put(CORRELATION_ID_HEADER, correlationId);

        try {
            chain.doFilter(httpRequest, response);
        } finally {
            MDC.remove(CORRELATION_ID_HEADER);
        }
    }
}

