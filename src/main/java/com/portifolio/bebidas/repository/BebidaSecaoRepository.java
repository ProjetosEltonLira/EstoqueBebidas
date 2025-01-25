package com.portifolio.bebidas.repository;

import com.portifolio.bebidas.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.entities.BebidaSecaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BebidaSecaoRepository extends JpaRepository <BebidaSecaoEntity, BebidaSecaoId> {
}
