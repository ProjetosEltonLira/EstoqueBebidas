package com.portifolio.bebidas.service;


import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.entities.TipoBebidaEntity;
import com.portifolio.bebidas.exceptions.TipoBebidaException;
import com.portifolio.bebidas.repository.TipoBebidaRepository;
import org.springframework.stereotype.Service;


@Service
public class TipoBebidaService {

    private final TipoBebidaRepository tipoBebidaRepository;

    public TipoBebidaService( TipoBebidaRepository tipoBebidaRepository) {
        this.tipoBebidaRepository = tipoBebidaRepository;
    }

    public TipoBebidaEntity getTipoBebida(String descricaoTipoBebida) {
        Integer codigoTipoBebida = TipoBebida.getCodigoByDescricao(descricaoTipoBebida);

        return tipoBebidaRepository.findById(Long.valueOf(codigoTipoBebida))
                .orElseThrow(() -> new TipoBebidaException("Tipo de bebida não encontrado!"));
    }

}
