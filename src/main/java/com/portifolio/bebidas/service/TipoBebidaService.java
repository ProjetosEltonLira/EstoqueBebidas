package com.portifolio.bebidas.service;

import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.entities.TipoBebidaEntity;
import com.portifolio.bebidas.exceptions.TipoDeBebidaNaoEncontradoException;
import com.portifolio.bebidas.repository.TipoBebidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TipoBebidaService {

    private static final Logger logger = LoggerFactory.getLogger(TipoBebidaService.class);

    private final TipoBebidaRepository tipoBebidaRepository;

    public TipoBebidaService( TipoBebidaRepository tipoBebidaRepository) {
        this.tipoBebidaRepository = tipoBebidaRepository;
    }

    public TipoBebidaEntity getTipoBebida(String descricaoTipoBebida) {
        Long codigoTipoBebida = Long.valueOf(TipoBebida.getCodigoByDescricao(descricaoTipoBebida));
        return findById(codigoTipoBebida);
    }

    private TipoBebidaEntity findById(Long codigoTipoBebida) {
        return tipoBebidaRepository.findById(codigoTipoBebida)
                .orElseThrow(() -> new TipoDeBebidaNaoEncontradoException("Tipo de bebida não encontrado!"));
    }
}
