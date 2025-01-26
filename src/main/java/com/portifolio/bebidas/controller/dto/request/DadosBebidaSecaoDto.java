package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosBebidaSecaoDto(

        @JsonProperty("id") Long id,

        @Min(value = 1)
        @JsonProperty("quantidade") Double quantidade
){}



