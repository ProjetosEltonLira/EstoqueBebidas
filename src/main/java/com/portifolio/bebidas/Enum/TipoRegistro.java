package com.portifolio.bebidas.Enum;

import com.portifolio.bebidas.exceptions.BebidasException;
import com.portifolio.bebidas.exceptions.TipoBebidaException;
import com.portifolio.bebidas.exceptions.TipoRegistroException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoRegistro {

    ENTRADA("ENTRADA", 1),
    SAIDA("SAIDA", 2);

    private final String descricao;
    private final Integer codigo;

    TipoRegistro(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
    public String getDescricao() {
        return descricao;
    }

    // Método para buscar o código a partir da descrição
    public static Integer getCodigoByDescricao(String descricao) {
        return Arrays.stream(TipoRegistro.values())
                .filter(tipo -> tipo.getDescricao().equalsIgnoreCase(descricao))
                .findFirst()
                .map(TipoRegistro::getCodigo)
                .orElseThrow(() -> new TipoRegistroException("Tipo de registro inválido, informar um dos tipos: "+ getDescricoesPermitidas()));
    }

    public static List<String> getDescricoesPermitidas() {
        return Arrays.stream(TipoRegistro.values())
                .map(TipoRegistro::getDescricao)
                .collect(Collectors.toList());
    }
}

