package com.portifolio.bebidas.enums;

import com.portifolio.bebidas.exceptions.TipoRegistroException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum TipoRegistro {

    ENTRADA("ENTRADA", 0),
    SAIDA("SAIDA", 1);

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

