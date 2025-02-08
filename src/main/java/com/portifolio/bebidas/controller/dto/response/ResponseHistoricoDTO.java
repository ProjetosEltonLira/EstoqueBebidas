package com.portifolio.bebidas.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record ResponseHistoricoDTO(

        @JsonProperty (value = "id") Long id,
        @JsonProperty (value = "data_registro") LocalDateTime dataRegistro,
        @JsonProperty (value = "tipo_registro") String tipo_registro,
        @JsonProperty (value = "bebida") ResponseBebidaHistoricoDTO bebida,
        @JsonProperty (value = "secao") ResponseSecaoHistoricoDTO secao
){}
