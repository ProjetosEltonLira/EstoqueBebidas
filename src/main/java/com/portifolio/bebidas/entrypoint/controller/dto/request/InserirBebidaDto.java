package com.portifolio.bebidas.entrypoint.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.portifolio.bebidas.entrypoint.validations.ValidTipoBebida;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record InserirBebidaDto(

        @NotNull @NotEmpty  (message = "Informe o nome da bebida")
        @JsonProperty("nome") String nomeBebida,

        @ValidTipoBebida
        @JsonProperty("tipo_bebida") @JsonAlias("tipoBebida") String tipoBebida

){}