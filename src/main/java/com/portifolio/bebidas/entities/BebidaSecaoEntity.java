package com.portifolio.bebidas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_secao_bebida")
public class BebidaSecaoEntity {

    @EmbeddedId
    private BebidaSecaoId id;

    @Min(value = 1)
    @Column(name = "quantidade_bebida")
    private Double quantidadeBebida;

    @CreationTimestamp
    @Column(name = "data_registro")
    private LocalDateTime dataCadastro;

    //  @UpdateTimestamp    // Quando for atualizar, o hibernate atualiza.
    //  @Column(name = "updated_at")
    //  private LocalDateTime dataAtualizacao;

    public BebidaSecaoEntity(){}
    public BebidaSecaoEntity(BebidaSecaoId id, Double quantidadeBebida) {
        this.quantidadeBebida = quantidadeBebida;
        this.id = id;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public BebidaSecaoId getId() {
        return id;
    }

    public void setId(BebidaSecaoId id) {
        this.id = id;
    }

    public Double getQuantidadeBebida() {
        return quantidadeBebida;
    }

    public void setQuantidadeBebida(Double quantidadeBebida) {
        this.quantidadeBebida = quantidadeBebida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BebidaSecaoEntity that = (BebidaSecaoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(quantidadeBebida, that.quantidadeBebida) && Objects.equals(dataCadastro, that.dataCadastro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantidadeBebida, dataCadastro);
    }
}
