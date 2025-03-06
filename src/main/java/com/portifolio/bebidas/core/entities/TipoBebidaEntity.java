package com.portifolio.bebidas.core.entities;


import com.portifolio.bebidas.core.enums.TipoBebida;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "tb_tipo_bebida")
public class TipoBebidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_bebida_id")
    private Long tipoBebidaId;

    @Column(name = "descricao")
    private String descricao;

    public TipoBebidaEntity() {}
    public TipoBebidaEntity(String descricao) {
        this.descricao = descricao;
    }
    public TipoBebidaEntity (Long tipoBebidaId, String descricao) {
        this.descricao = descricao;
        this.tipoBebidaId = tipoBebidaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isTipo(TipoBebida tipoBebidaEnum){
        return descricao.equals(tipoBebidaEnum.name());
    }

    public Long getTipoBebidaId() {
        return tipoBebidaId;
    }

    public void setTipoBebidaId(Long tipoBebidaId) {
        this.tipoBebidaId = tipoBebidaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoBebidaEntity that = (TipoBebidaEntity) o;
        return Objects.equals(tipoBebidaId, that.tipoBebidaId) && Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoBebidaId, descricao);
    }
}



