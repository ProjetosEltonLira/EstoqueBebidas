package com.portifolio.bebidas.core.entities.mapper;

import com.portifolio.bebidas.core.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.core.entities.SecaoEntity;
import com.portifolio.bebidas.core.entities.TipoBebidaEntity;
import com.portifolio.bebidas.core.enums.TipoBebida;
import com.portifolio.bebidas.entrypoint.controller.dto.response.BebidasNaSecaoResponseDto;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecaoMapperTest {

    SecaoMapper mapper = Mappers.getMapper(SecaoMapper.class);

//    @Nested
//    class testeMapper {
//        @Test
//        @DisplayName("Teste 12 - Testar o secaoMapper.")
//        void deveMapearSecaoEntityParaResponseSecaoDtoCorretamente() {
//
//            // Criando um mock de BebidasNaSecaoResponseDto
//            BebidasNaSecaoResponseDto bebidaResponseDto = new BebidasNaSecaoResponseDto(1L, "teste", "ALCOOLICA", 50.0, LocalDateTime.now());
//
//            SecaoEntity secaoEntity = new SecaoEntity(1L,"teste",
//                    new TipoBebidaEntity(1L,"ALCOOLICA"),
//                    List.of(new BebidaSecaoEntity()))
//
//
//            // Chamando o mapper
//            ResponseSecaoDto responseDto = mapper.toResponseSecaoDto(secaoEntity);
//
//            // Verificando os mapeamentos
//            assertNotNull(responseDto);
//            assertEquals(responseDto.secaoId(), secaoAlcoolica.getSecaoId());
//            assertEquals(responseDto.nomeSecao(), secaoAlcoolica.getNomeSecao());
//            assertEquals(responseDto.tipoSecao(), secaoAlcoolica.getTipoBebida().getDescricao());
//            assertEquals(responseDto.quantidadeTotal(), 300.0);
//        }
//    }
}