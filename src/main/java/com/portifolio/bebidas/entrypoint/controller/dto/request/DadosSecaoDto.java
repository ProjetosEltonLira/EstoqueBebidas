package com.portifolio.bebidas.entrypoint.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portifolio.bebidas.entrypoint.validations.ValidTipoBebida;
import jakarta.validation.constraints.NotEmpty;

public record DadosSecaoDto(

        @NotEmpty(message = "Informe um nome para a secao")
        @JsonProperty("nome") String nome,

        @ValidTipoBebida //Quando tem validações personalidade, adicionar o Alias como nome da variável, dessa forma o valid também aceita.
        @JsonProperty("tipo_bebida") @JsonAlias("tipoBebida")String tipoBebida
){}



