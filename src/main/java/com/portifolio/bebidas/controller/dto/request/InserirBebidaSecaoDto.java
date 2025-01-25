package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public record InserirBebidaSecaoDto(

        @NotEmpty(message = "Informe o tipo de registro, [ENTRADA ou SAIDA]")
        @JsonProperty("descricao_tipo_registro") String tipoRegistro,

        @NotEmpty(message = "Informe pelo menos uma bebida")
        @JsonProperty("bebidas") List<DadosBebidaSecaoDto> bebidas
){}



