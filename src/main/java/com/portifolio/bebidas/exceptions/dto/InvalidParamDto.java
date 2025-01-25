package com.portifolio.bebidas.exceptions.dto;

public record InvalidParamDto(
        String field,
        String reason
){}

