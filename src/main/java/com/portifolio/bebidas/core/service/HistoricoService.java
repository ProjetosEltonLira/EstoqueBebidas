package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.entities.HistoricoEntity;
import com.portifolio.bebidas.core.repository.HistoricoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    private static final Logger logger = LoggerFactory.getLogger(HistoricoService.class);

    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;

    }

    public Page<HistoricoEntity> procurarHistoricoDaBebida(int page, int pageSize, String orderBy, Long bebidaId) {

        var direction = Sort.Direction.DESC; // Define a direção da ordenação como decrescente
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        var pageRequest = PageRequest.of(page, pageSize, direction, "data_registro");

        return historicoRepository.findAllHistoricoBebida(bebidaId,pageRequest);
    }
}
