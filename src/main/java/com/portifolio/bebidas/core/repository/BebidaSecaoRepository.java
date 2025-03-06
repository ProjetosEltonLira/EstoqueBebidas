package com.portifolio.bebidas.core.repository;

import com.portifolio.bebidas.core.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.core.entities.BebidaSecaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BebidaSecaoRepository extends JpaRepository <BebidaSecaoEntity, BebidaSecaoId> {
}
