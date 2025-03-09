package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.entrypoint.controller.dto.request.InserirBebidaDto;
import com.portifolio.bebidas.core.entities.BebidaEntity;
import com.portifolio.bebidas.core.entities.HistoricoEntity;
import com.portifolio.bebidas.core.exceptions.BebidaNaoEncontradaException;
import com.portifolio.bebidas.core.repository.BebidaRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BebidaService {

    private static final Logger logger = LoggerFactory.getLogger(BebidaService.class);

    private final BebidaRepository bebidaRepository;
    private final TipoBebidaService tipoBebidaService;
    private final HistoricoService historicoService;

    public BebidaService(BebidaRepository bebidaRepository, TipoBebidaService tipoBebidaService, HistoricoService historicoService) {
        this.bebidaRepository = bebidaRepository;
        this.tipoBebidaService = tipoBebidaService;
        this.historicoService = historicoService;
    }

    public BebidaEntity inserirBebida(@Valid InserirBebidaDto bebidaDto) {

        var tipoBebidaEntity = tipoBebidaService.getTipoBebida(bebidaDto.tipoBebida());
        var bebidaEntity = new BebidaEntity(bebidaDto.nomeBebida(), tipoBebidaEntity);

        return bebidaRepository.save(bebidaEntity);
    }

    public Page<BebidaEntity> findAll(int page, int pageSize, String orderBy) {

        var direction = Sort.Direction.DESC;
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        var pageRequest = PageRequest.of(page, pageSize, direction, "data_cadastro");

        return bebidaRepository.findAll(pageRequest);
    }

    public BebidaEntity findById(Long bebidaId) {
        return bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new BebidaNaoEncontradaException("Bebida com o Id " + bebidaId + " não foi encontrada, para utilizar ela efetue o cadastro da bebida"));
    }

    public Page<HistoricoEntity> procurarHistoricoDaBebida(int page, int pageSize, String orderBy, Long bebidaId) {

          return historicoService.procurarHistoricoDaBebida(page, pageSize, orderBy, bebidaId);
    }
}
