package com.portifolio.bebidas.enums;

import com.portifolio.bebidas.exceptions.TipoBebidaException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoBebida {

    ALCOOLICA("ALCOOLICA", 1),
    SEM_ALCOOL("SEM ALCOOL", 2),
 	REFRIGERANTE("REFRIGERANTE", 3),
	SUCO("SUCO",4),
	CERVEJA("CERVEJA",5),
	VINHO("VINHO",6),
	AGUA("AGUA",7);

    private final String descricao;
    private final Integer codigo;

    TipoBebida(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
    public String getDescricao() {
        return descricao;
    }


    public static Integer getCodigoByDescricao(String descricao) {
        return Arrays.stream(TipoBebida.values())
                .filter(tipo -> tipo.getDescricao().equalsIgnoreCase(descricao))
                .findFirst()
                .map(TipoBebida::getCodigo)
                .orElseThrow(() -> new TipoBebidaException("Tipo de bebida " + descricao + " não encontrada, tipo de bebidas permitidos: " + getDescricoesPermitidas()));
    }

    public static List<String> getDescricoesPermitidas() {
        return Arrays.stream(TipoBebida.values())
                .map(TipoBebida::getDescricao)
                .collect(Collectors.toList());
    }
}

