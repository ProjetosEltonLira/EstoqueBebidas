package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record InserirBebidaDto(

        @NotNull @NotEmpty  (message = "Informe o nome da bebida")
        @JsonProperty("nome") String nomeBebida,

        @NotEmpty(message = "Informe o tipo da bebida ex: [ALCOOLICA, SEM ALCOOL]")
        @JsonProperty("tipo_bebida") String tipoBebida

){}