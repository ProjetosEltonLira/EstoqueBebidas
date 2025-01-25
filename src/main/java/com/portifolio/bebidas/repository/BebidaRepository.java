package com.portifolio.bebidas.repository;

import com.portifolio.bebidas.entities.BebidaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BebidaRepository extends JpaRepository <BebidaEntity,Long> {


    @Query(value = "SELECT * FROM tb_bebida",
            countQuery = "SELECT COUNT(*) FROM tb_bebida",
            nativeQuery = true)
    Page<BebidaEntity> findAll (PageRequest pagaRequest);
}
