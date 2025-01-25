package com.portifolio.bebidas.controller;

import com.portifolio.bebidas.Service.SecaoService;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;


import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.controller.dto.response.Data;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Objects.isNull;

@RestController
@RequestMapping (path = "/secao")
public class SecaoController {

    private final SecaoService secaoService ;

    public SecaoController(SecaoService secaoService) {
        this.secaoService = secaoService;

    }

    @PostMapping
    public ResponseEntity<Void> criarSecao (@Valid @RequestBody SecaoDto dto){

        var response = secaoService.criarSecao(dto);

        return ResponseEntity.created(URI.create("/secao/"+response.getSecaoId())).build();
    }

    @PostMapping(value = "/{idSecao}")
    public ResponseEntity<Void> cadastrarBebidaNaSecao (
            @PathVariable Long idSecao,
            @Valid @RequestBody InserirBebidaSecaoDto bebidasDto){

        var response = secaoService.cadastrarBebidas(idSecao, bebidasDto);

        return ResponseEntity.created(URI.create("/secao/"+response.getSecaoId())).build();
    }


    @GetMapping(path = "/{idSecao}")
    public ResponseEntity<Data<ResponseSecaoDto>> buscarBebidasNaSecao(
            @PathVariable Long idSecao){

        var response = secaoService.procurarSecaoPorId(idSecao);

        if (isNull(response))
            return ResponseEntity.notFound().build();
        else
            return new ResponseEntity<>(new Data<>(response), HttpStatus.OK);

    }
}
