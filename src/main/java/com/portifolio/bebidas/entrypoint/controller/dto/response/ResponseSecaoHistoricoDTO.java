package com.portifolio.bebidas.entrypoint.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseSecaoHistoricoDTO (

    @JsonProperty(value = "id") Long id,
    @JsonProperty(value = "nome") String nome
){}
