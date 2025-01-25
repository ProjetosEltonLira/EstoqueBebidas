package com.portifolio.bebidas.service;


import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaDto;
import com.portifolio.bebidas.entities.BebidaEntity;
import com.portifolio.bebidas.exceptions.BebidasException;
import com.portifolio.bebidas.exceptions.TipoBebidaException;
import com.portifolio.bebidas.repository.BebidaRepository;
import com.portifolio.bebidas.repository.TipoBebidaRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class BebidaService {

    private final BebidaRepository bebidaRepository;
    private final TipoBebidaRepository tipoBebidaRepository;

    public BebidaService(BebidaRepository bebidaRepository, TipoBebidaRepository tipoBebidaRepository) {
        this.bebidaRepository = bebidaRepository;
        this.tipoBebidaRepository = tipoBebidaRepository;
    }

    public BebidaEntity inserirBebida(@Valid InserirBebidaDto bebidaDto) {

        Integer codigoBebida = TipoBebida.getCodigoByDescricao(bebidaDto.tipoBebida());
        var tipoBebida = tipoBebidaRepository.findById(Long.valueOf(codigoBebida))
                .orElseThrow(() -> new TipoBebidaException("Tipo de Bebida não encontrado"));

        var bebida = new BebidaEntity(bebidaDto.nomeBebida(), tipoBebida);

        return bebidaRepository.save(bebida);
    }

    public Page<BebidaEntity> findAll(int page, int pageSize, String orderBy) {

        var direction = Sort.Direction.DESC; // Define a direção da ordenação como decrescente
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        var pageRequest = PageRequest.of(page, pageSize, direction, "data_cadastro");

        return bebidaRepository.findAll(pageRequest);
    }

    public BebidaEntity findById(Long bebidaId) {
        return bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new BebidasException("Bebida com o Id " + bebidaId+ " não encontrada"));
    }

}
