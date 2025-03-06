package com.portifolio.bebidas.entrypoint.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final HeaderValidacaoInterceptor headerValidacaoInterceptor;

    public WebConfig(HeaderValidacaoInterceptor headerValidacaoInterceptor) {
        this.headerValidacaoInterceptor = headerValidacaoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerValidacaoInterceptor);
    }
}
