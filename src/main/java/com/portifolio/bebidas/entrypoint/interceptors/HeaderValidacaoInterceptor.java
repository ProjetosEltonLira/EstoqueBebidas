package com.portifolio.bebidas.entrypoint.interceptors;

import com.portifolio.bebidas.core.exceptions.HeaderObrigatoriosException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderValidacaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader("x-correlation-id");
        String appId = request.getHeader("x-app-id");
        String apiKey = request.getHeader("x-api-key");

        if (correlationId == null || appId == null || apiKey == null) {
            throw new HeaderObrigatoriosException("Obrigatório o envio dos headers: x-correlation-id, x-app-id, x-api-key");
        }

        return true; // Continua para o controller
    }
}
