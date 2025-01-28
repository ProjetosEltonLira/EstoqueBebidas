package com.portifolio.bebidas.enums;

import com.portifolio.bebidas.exceptions.TipoDeBebidaNaoEncontradoException;

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

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    TipoBebida(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    /**
     * Retorna o código do tipo de bebida baseado na descrição fornecida.
     * Lança uma exceção se a descrição não for válida.
     *
     * @param descricaoTipoBebida descrição do tipo de bebida
     * @return código associado ao tipo de bebida
     */
    public static Integer getCodigoByDescricao(String descricaoTipoBebida) {
        return Arrays.stream(values())
                .filter(tipo -> tipo.descricao.equalsIgnoreCase(descricaoTipoBebida))
                .findFirst()
                .map(TipoBebida::getCodigo)
                .orElseThrow(() -> new TipoDeBebidaNaoEncontradoException(
                        "Tipo de bebida '" + descricaoTipoBebida + "' não encontrado. Tipos permitidos: " + getDescricoesPermitidas()
                ));
    }


    /**
     * Retorna uma lista com todas as descrições permitidas.
     *
     * @return lista de descrições
     */
    public static List<String> getDescricoesPermitidas() {
        return Arrays.stream(values())
                .map(TipoBebida::getDescricao)
                .collect(Collectors.toList());
    }
}

