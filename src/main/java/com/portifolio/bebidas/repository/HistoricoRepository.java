package com.portifolio.bebidas.repository;


import com.portifolio.bebidas.entities.HistoricoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.UUID;

public interface HistoricoRepository extends JpaRepository <HistoricoEntity, UUID> {

    @Query(value = "SELECT * FROM tb_historico where bebida_id = ?1",
            countQuery = "SELECT COUNT(*) FROM tb_historico where bebida_id = ?1",
            nativeQuery = true)
    Page<HistoricoEntity> findAllHistoricoBebida (Long bebida_id,PageRequest pagaRequest);
}
