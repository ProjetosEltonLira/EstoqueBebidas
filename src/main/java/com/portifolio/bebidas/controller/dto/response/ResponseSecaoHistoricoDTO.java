package com.portifolio.bebidas.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseSecaoHistoricoDTO (

    @JsonProperty(value = "id") Long id,
    @JsonProperty(value = "nome_") String nome
){}
