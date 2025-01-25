package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record DadosSecaoDto(

        @NotEmpty (message = "Informe um nome para a secao")
        @JsonProperty("nome") String nome,

        @NotEmpty (message = "Informe o tipo bebida da secao")
        @JsonProperty("tipo_bebida") String tipoBebida
){}



