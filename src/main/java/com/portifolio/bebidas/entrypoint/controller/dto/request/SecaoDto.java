package com.portifolio.bebidas.entrypoint.controller.dto.request;


import jakarta.validation.Valid;

public record SecaoDto(

        @Valid
        DadosSecaoDto secao
){}



