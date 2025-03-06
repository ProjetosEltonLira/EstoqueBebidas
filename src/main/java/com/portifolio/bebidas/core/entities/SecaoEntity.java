package com.portifolio.bebidas.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tb_secao")
public class SecaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "secao_id")
    private Long secaoId;

    @Column(name = "secao_nome")
    private String nomeSecao;

    @ManyToOne
    @JoinColumn(name = "tipo_bebida_id", nullable = false)
    private TipoBebidaEntity tipoBebida;

    @JsonIgnore
    @OneToMany (mappedBy = "id.secao",cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<BebidaSecaoEntity> bebidaSecaoEntities = new ArrayList<>();

    public SecaoEntity(){}
    public SecaoEntity( Long secaoId, String nomeSecao, TipoBebidaEntity tipoBebida, List<BebidaSecaoEntity> bebidaSecaoEntities) {
        this.bebidaSecaoEntities = bebidaSecaoEntities;
        this.nomeSecao = nomeSecao;
        this.secaoId = secaoId;
        this.tipoBebida = tipoBebida;
    }

    public SecaoEntity(String nomeSecao,TipoBebidaEntity tipoBebida) {
        this.nomeSecao = nomeSecao;
        this.tipoBebida = tipoBebida;
    }



    public List<BebidaSecaoEntity> getBebidaSecaoEntities() {
        return bebidaSecaoEntities;
    }

    public void setBebidaSecaoEntities(List<BebidaSecaoEntity> bebidaSecaoEntities) {
        this.bebidaSecaoEntities = bebidaSecaoEntities;
    }

    public String getNomeSecao() {
        return nomeSecao;
    }

    public void setNomeSecao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }

    public Long getSecaoId() {
        return secaoId;
    }

    public void setSecaoId(Long secaoId) {
        this.secaoId = secaoId;
    }

    public TipoBebidaEntity getTipoBebida() {
        return tipoBebida;
    }


    public void setTipoBebida(TipoBebidaEntity tipoBebida) {
        this.tipoBebida = tipoBebida;
    }


}