package com.portifolio.bebidas.controller.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portifolio.bebidas.validations.ValidTipoBebida;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DadosSecaoDto(

        @NotEmpty(message = "Informe um nome para a secao")
        @JsonProperty("nome") String nome,

        @ValidTipoBebida //Quando tem validações personalidade, adicionar o Alias como nome da variável, dessa forma o valid também aceita.
        @JsonProperty("tipo_bebida") @JsonAlias("tipoBebida")String tipoBebida
){}



