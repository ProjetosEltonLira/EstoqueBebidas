package com.portifolio.bebidas.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ResponseSecaoDto(

        @JsonProperty (value = "id_secao") Long secaoId,
        @JsonProperty (value = "nome_secao") String nomeSecao,
        @JsonProperty (value = "tipo_secao") String tipoSecao,
        @JsonProperty (value = "quantidade_total") Double quantidadeTotal,
        @JsonProperty (value = "bebidas") List<BebidasNaSecaoResponseDto> bebidas
){
}
