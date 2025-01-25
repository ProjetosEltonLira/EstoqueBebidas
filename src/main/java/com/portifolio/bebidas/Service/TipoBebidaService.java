package com.portifolio.bebidas.Service;


import com.portifolio.bebidas.Enum.TipoBebida;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaDto;
import com.portifolio.bebidas.entities.BebidaEntity;
import com.portifolio.bebidas.entities.TipoBebidaEntity;
import com.portifolio.bebidas.exceptions.TipoBebidaException;
import com.portifolio.bebidas.repository.BebidaRepository;
import com.portifolio.bebidas.repository.TipoBebidaRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
