package com.portifolio.bebidas.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record BebidasNaSecaoResponseDto(

        @JsonProperty (value = "bebida_id") Long bebidaId,
        @JsonProperty (value = "nome_bebida") String nomeBebida,
        @JsonProperty (value = "tipo_bebida") String tipoBebida,
        @JsonProperty (value = "quantidade") Double quantidadeBebida,
        @JsonProperty (value = "data_cadastro") LocalDateTime dataCadastro
) {
}
