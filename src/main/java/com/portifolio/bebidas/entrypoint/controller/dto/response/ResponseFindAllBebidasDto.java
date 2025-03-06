package com.portifolio.bebidas.entrypoint.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ResponseFindAllBebidasDto(

        @NotNull @NotEmpty  (message = "Informe o Id da bebida")
        @JsonProperty("id") Long id,

        @NotNull @NotEmpty  (message = "Informe o nome da bebida")
        @JsonProperty("nome") String nomeBebida,

        @NotEmpty(message = "Informe o tipo da bebida ex: [ALCOOLICA, SEM ALCOOL]")
        @JsonProperty("tipo_bebida") String tipoBebida,

        @JsonProperty("data_cadastro")
        LocalDateTime dataCadastro
){}