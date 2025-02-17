package com.portifolio.bebidas.controller;

import com.portifolio.bebidas.entities.HistoricoEntity;
import com.portifolio.bebidas.entities.mapper.HistoricoMapper;
import com.portifolio.bebidas.service.BebidaService;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaDto;
import com.portifolio.bebidas.controller.dto.response.ApiResponseDto;
import com.portifolio.bebidas.controller.dto.response.PaginationResponseDTO;

import com.portifolio.bebidas.entities.BebidaEntity;
import com.portifolio.bebidas.entities.mapper.BebidaMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping (path = "/bebidas")
public class BebidaController {

    private final BebidaService bebidaService;
    private final BebidaMapper bebidaMapper;
    private final HistoricoMapper historicoMapper;

    public BebidaController(BebidaService bebidaService, BebidaMapper bebidaMapper, HistoricoMapper historicoMapper) {
        this.bebidaService = bebidaService;
        this.bebidaMapper = bebidaMapper;
        this.historicoMapper = historicoMapper;
    }

    @PostMapping
    public ResponseEntity<Void> inserirBebida(@Valid @RequestBody InserirBebidaDto dto){

        var response = bebidaService.inserirBebida(dto);

        return ResponseEntity.created(URI.create("/bebidas/"+ response.getBebidaId())).build();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<BebidaEntity>> buscarTodasBebidas(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy){

        var pageResponse = bebidaService.findAll(page,pageSize,orderBy);

        return ResponseEntity.ok(
            new ApiResponseDto(
                    bebidaMapper.toResponseFindAllBebidasDtoList(pageResponse.getContent()),
                    new PaginationResponseDTO(
                            pageResponse.getNumber(),
                            pageResponse.getSize(),
                            pageResponse.getTotalElements(),
                            pageResponse.getTotalPages())
            ));
    }


    @GetMapping ( path = "/{bebidaId}/historico")
    public ResponseEntity<ApiResponseDto<Page<HistoricoEntity>>> buscarHistoricoDaBebida(
            @PathVariable Long bebidaId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy){

        Page<HistoricoEntity> pageResponse = bebidaService.procurarHistoricoDaBebida(page,pageSize,orderBy,bebidaId);

        return ResponseEntity.ok(
                new ApiResponseDto(
                        //pageResponse.getContent(),
                        historicoMapper.toListResponseDTO(pageResponse.getContent()),
                        new PaginationResponseDTO(
                                pageResponse.getNumber(),
                                pageResponse.getSize(),
                                pageResponse.getTotalElements(),
                                pageResponse.getTotalPages())

                ));
    }
}
