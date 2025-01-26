package com.portifolio.bebidas.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.portifolio.bebidas.enums.TipoRegistro;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "tb_historico")
public class HistoricoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "historico_id")
    private UUID historicoId;

    @CreationTimestamp
    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_registro")
    private TipoRegistro tipoRegistro;

    @Column(name = "nome_solicitante", nullable = false)
    private String nomeSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "secao_id", nullable = false)
    private SecaoEntity secao;

    public HistoricoEntity() {}
    public HistoricoEntity(String nomeSolicitante, TipoRegistro tipoRegistro, SecaoEntity secao) {
        this.nomeSolicitante = nomeSolicitante;
        this.tipoRegistro = tipoRegistro;
        this.secao = secao;
    }
    public HistoricoEntity(String nomeSolicitante, TipoRegistro tipoRegistro) {
        this.nomeSolicitante = nomeSolicitante;
        this.tipoRegistro = tipoRegistro;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public UUID getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(UUID historicoId) {
        this.historicoId = historicoId;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public SecaoEntity getSecao() {
        return secao;
    }

    public void setSecao(SecaoEntity secao) {
        this.secao = secao;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoEntity that = (HistoricoEntity) o;
        return Objects.equals(historicoId, that.historicoId) && Objects.equals(dataRegistro, that.dataRegistro) && tipoRegistro == that.tipoRegistro && Objects.equals(nomeSolicitante, that.nomeSolicitante) && Objects.equals(secao, that.secao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historicoId, dataRegistro, tipoRegistro, nomeSolicitante, secao);
    }

    @Override
    public String toString() {
        return "HistoricoEntity{" +
                "dataRegistro='" + dataRegistro + '\'' +
                ", historicoId=" + historicoId +
                ", tipoRegistro=" + tipoRegistro +
                ", nomeFuncionario='" + nomeSolicitante + '\'' +
                ", secaoEntity=" + secao +
                '}';
    }
}

