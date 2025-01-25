package com.portifolio.bebidas.Service;


import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

    private final Double LIMITE_BEBIDA_ALCOOLICA = 500.0;
    private final Double LIMITE_BEBIDA_SEM_ALCOOL = 400.0;

    private Boolean validarRegras(SecaoEntity secao, List<DadosBebidaSecaoDto> BebidasAseremCadastradas) {




//
//        if (secao.getTipoBebida().getDescricao().equals(TipoBebida.ALCOOLICA.toString()))
//            if (totalBebidaNaSecao > 500)
//                throw new BebidasException("A quantidade máxima de bebida na secao foi atiginda, procurei outra secao");

        return true;
    }
}




