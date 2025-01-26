package com.portifolio.bebidas.repository;

import com.portifolio.bebidas.entities.HistoricoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoricoRepository extends JpaRepository <HistoricoEntity, UUID> {
}
