package com.portifolio.bebidas.controller.dto.request;


import jakarta.validation.Valid;

public record SecaoDto(

        @Valid
        DadosSecaoDto secao
){}



