package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.enums.TipoBebida;
import com.portifolio.bebidas.core.entities.TipoBebidaEntity;
import com.portifolio.bebidas.core.exceptions.TipoDeBebidaNaoEncontradoException;
import com.portifolio.bebidas.core.repository.TipoBebidaRepository;
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
