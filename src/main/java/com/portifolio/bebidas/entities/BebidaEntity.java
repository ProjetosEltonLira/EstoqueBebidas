package com.portifolio.bebidas.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table (name = "tb_bebida")
public class BebidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty (value = "id")
    @Column(name = "bebida_id")
    private Long bebidaId;

    @JsonProperty (value = "nome")
    @Column(name = "nome_bebida", nullable = false)
    private String nomeBebida;

    @ManyToOne
    @JsonProperty (value = "tipo")
    //@Enumerated(EnumType.STRING) // ou EnumType.ORDINAL, dependendo do banco
    @JoinColumn(name = "tipo_bebida_id", nullable = false)
    private TipoBebidaEntity tipoBebida;

    @CreationTimestamp   // quando for criar, o hibernate cria a data.
    @JsonProperty (value = "data_cadastro")
    @Column(name = "data_cadastro",  nullable = false)
    private LocalDateTime dataCadastro;

    //@OneToMany (mappedBy = "id.bebida",cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    //private List<BebidaSecaoEntity> bebidaSecaoEntities = new ArrayList<>();

    public BebidaEntity(){}
    public BebidaEntity(String nomeBebida, TipoBebidaEntity tipoBebida) {
        this.nomeBebida = nomeBebida;

        this.tipoBebida = tipoBebida;
    }
    public BebidaEntity(Long bebidaId, String nomeBebida, TipoBebidaEntity tipoBebida) {
        this.bebidaId = bebidaId;
        this.nomeBebida = nomeBebida;
        this.tipoBebida = tipoBebida;
    }

    public Long getBebidaId() {
        return bebidaId;
    }

    public void setBebidaId(Long bebidaId) {
        this.bebidaId = bebidaId;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getNomeBebida() {
        return nomeBebida;
    }

    public void setNomeBebida(String nomeBebida) {
        this.nomeBebida = nomeBebida;
    }

    public TipoBebidaEntity getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(TipoBebidaEntity tipoBebida) {
        this.tipoBebida = tipoBebida;
    }
}
