package com.portifolio.bebidas.entrypoint.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ResponseHistoricoDTO(

        @JsonProperty (value = "id") String id,
        @JsonProperty (value = "data_registro") LocalDateTime dataRegistro,
        @JsonProperty (value = "tipo_registro") String tipoRegistro,
        @JsonProperty (value = "bebida") ResponseBebidaHistoricoDTO bebida,
        @JsonProperty (value = "secao") ResponseSecaoHistoricoDTO secao

){}
