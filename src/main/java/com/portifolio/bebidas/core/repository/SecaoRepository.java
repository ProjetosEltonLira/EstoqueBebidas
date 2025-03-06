package com.portifolio.bebidas.core.repository;

import com.portifolio.bebidas.core.entities.SecaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SecaoRepository extends JpaRepository <SecaoEntity,Long> {

   String SQL_STATEMENT =
            """ 
                Select *
                FROM
                  tb_secao
            """;
   String  SQL_COUNT_STATEMENT =
            """ 
                Select count(*) FROM
                (
                """ + SQL_STATEMENT + """
                ) as total
            """;
    @Query(value = SQL_COUNT_STATEMENT, countQuery = SQL_COUNT_STATEMENT, nativeQuery = true)
    public long quantidadeSecoesAtivas ();
}
