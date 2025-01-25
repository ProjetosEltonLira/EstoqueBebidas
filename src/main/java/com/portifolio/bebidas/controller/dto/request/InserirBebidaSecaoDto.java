package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.portifolio.bebidas.validations.ValidTipoRegistro;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record InserirBebidaSecaoDto(

        //implementado uma validação personalizada para validar o tipo de registro
        @ValidTipoRegistro//Quando tem validações personalidade, adicionar o Alias como nome da variável, dessa forma o valid também aceita.
        @JsonProperty("tipo_registro") @JsonAlias("tipoRegistro") String tipoRegistro,

        @NotEmpty(message = "Informe pelo menos uma bebida")
        @JsonProperty("bebidas") List<DadosBebidaSecaoDto> bebidas
){}



