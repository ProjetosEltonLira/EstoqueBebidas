package com.portifolio.bebidas.entrypoint.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record DadosBebidaSecaoDto(

        @JsonProperty("id") Long id,

        @Min(value = 1)
        @JsonProperty("quantidade") Double quantidade
){}



