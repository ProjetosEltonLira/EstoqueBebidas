package com.portifolio.bebidas.entrypoint.controller;

import com.portifolio.bebidas.core.service.SecaoService;
import com.portifolio.bebidas.entrypoint.controller.dto.request.InserirBebidaSecaoDto;


import com.portifolio.bebidas.entrypoint.controller.dto.request.SecaoDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.Data;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoDTO;

import com.portifolio.bebidas.core.utils.HeaderUtil;
import com.portifolio.bebidas.core.utils.JsonUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Objects.isNull;

@RestController
@RequestMapping (path = "/secao")
public class SecaoController {

    private static final Logger logger = LoggerFactory.getLogger(SecaoController.class);

    private final SecaoService secaoService ;

    public SecaoController(SecaoService secaoService) {
        this.secaoService = secaoService;

    }

    @PostMapping
    public ResponseEntity<Void> criarSecao (@Valid @RequestBody SecaoDTO dto) {
        logger.info("Recebida solicitação para criar seção: {}", JsonUtil.toJson(dto));

            var response = secaoService.criarSecao(dto);
            logger.info("Seção criada com sucesso. ID: {}", response.getSecaoId());

            return ResponseEntity
                    .created(URI.create("/secao/" + response.getSecaoId()))
                    .headers(HeaderUtil.getCorrelationId()).build();
    }

    @PostMapping(value = "/{idSecao}")
    public ResponseEntity<Void> cadastrarBebidaNaSecao (
            @PathVariable Long idSecao,
            @Valid @RequestBody InserirBebidaSecaoDto bebidasDto){

        var response = secaoService.cadastrarBebidas(idSecao, bebidasDto);

        return ResponseEntity.created(URI.create("/secao/"+response.getSecaoId())).build();
    }


    @GetMapping(path = "/{idSecao}")
    public ResponseEntity<Data<ResponseSecaoDTO>> buscarBebidasNaSecao(
            @PathVariable Long idSecao){

        var response = secaoService.procurarSecaoPorId(idSecao);

        if (isNull(response))
            return ResponseEntity.notFound().build();
        else
            return new ResponseEntity<>(new Data<>(response), HttpStatus.OK);

    }
}
