package com.portifolio.bebidas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Objects;


@Embeddable
public class BebidaSecaoId {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "secao_id")
    private SecaoEntity secao;

    @ManyToOne
    @JoinColumn(name = "bebida_id")
    private BebidaEntity bebida;

    public BebidaSecaoId() {}
    public BebidaSecaoId(BebidaEntity bebida, SecaoEntity secao) {
        this.bebida = bebida;
        this.secao = secao;
    }

    public BebidaEntity getBebida() {
        return bebida;
    }

    public void setBebida(BebidaEntity bebida) {
        this.bebida = bebida;
    }

    public SecaoEntity getSecao() {
        return secao;
    }

    public void setSecao(SecaoEntity secao) {
        this.secao = secao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BebidaSecaoId id = (BebidaSecaoId) o;
        return Objects.equals(secao, id.secao) && Objects.equals(bebida, id.bebida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secao, bebida);
    }
}
