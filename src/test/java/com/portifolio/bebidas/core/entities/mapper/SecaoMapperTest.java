package com.portifolio.bebidas.core.entities.mapper;

import com.portifolio.bebidas.core.entities.*;
import com.portifolio.bebidas.core.mapper.BebidaMapper;
import com.portifolio.bebidas.core.mapper.SecaoMapper;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoHistoricoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SecaoMapperTest {

    @InjectMocks
    private SecaoMapper secaoMapper = Mappers.getMapper(SecaoMapper.class);

    @Mock
    BebidaMapper bebidaMapper = Mappers.getMapper(BebidaMapper.class);

    TipoBebidaEntity tipoBebidaAlcoolica;
    TipoBebidaEntity tipoBebidaSemAlcool;
    BebidaEntity bebida1;
    BebidaEntity bebida2;
    BebidaEntity bebida3;
    SecaoEntity secaoAlcoolica;
    List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();

    @BeforeEach
    void setUp() {

        secaoMapper = Mappers.getMapper(SecaoMapper.class);
        bebidaMapper = Mappers.getMapper(BebidaMapper.class);

        tipoBebidaAlcoolica = new TipoBebidaEntity(1L, "ALCOOLICA");
        tipoBebidaSemAlcool = new TipoBebidaEntity(2L, "SEM ALCOOL");

        bebida1 = new BebidaEntity(1L, "Cachaça", tipoBebidaAlcoolica);
        bebida2 = new BebidaEntity(2L, "SELVAGEM", tipoBebidaAlcoolica);
        bebida3 = new BebidaEntity(3L, "SAQUE", tipoBebidaAlcoolica);

        secaoAlcoolica = new SecaoEntity(1L, "SECAO ALCOOLICA", tipoBebidaAlcoolica, null);

        listBebidasNaSecao.add(new BebidaSecaoEntity(new BebidaSecaoId(bebida1, secaoAlcoolica), 90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(new BebidaSecaoId(bebida2, secaoAlcoolica), 100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(new BebidaSecaoId(bebida3, secaoAlcoolica), 110.0));

        secaoAlcoolica.setBebidaSecaoEntities(listBebidasNaSecao);
    }
    @Nested
    class testeMapperSecao {

        @Test
        @DisplayName("Deve Mapear o SecaoEntity Para o ResponseSecaoHistoricoDTO")
        void deveMapearSecaoEntityParaResponseSecaoHistoricoDTO() {

            ResponseSecaoHistoricoDTO responseSecaoHistoricoDTO = secaoMapper.toResponseSecaoHistoricoDTO(secaoAlcoolica);

            assertNotNull(responseSecaoHistoricoDTO);
            assertEquals(1L, responseSecaoHistoricoDTO.id());
            assertEquals("SECAO ALCOOLICA", responseSecaoHistoricoDTO.nome());
        }

//        @Test
//        @DisplayName("Deve Mapear o SecaoEntity Para o ResponseSecaoDTO")
//        void deveMapearSecaoEntitytoResponseSecaoDto() {
//
//            ResponseSecaoDTO responseSecaoDTO = secaoMapper.toResponseSecaoDto(secaoAlcoolica);
//
//            assertNotNull(responseSecaoDTO);
//            assertEquals(1L, responseSecaoDTO.secaoId());
//            assertEquals("SECAO ALCOOLICA", responseSecaoDTO.nomeSecao());
//            assertEquals(300.0, responseSecaoDTO.quantidadeTotal());
//            assertEquals("ALCOOLICA", responseSecaoDTO.tipoSecao());
//            assertEquals(1L, responseSecaoDTO.bebidas().get(0).bebidaId());
//            assertEquals("Cachaça", responseSecaoDTO.bebidas().get(0).nomeBebida());
//            assertEquals(90.0, responseSecaoDTO.bebidas().get(0).quantidadeBebida());
//            assertEquals("ALCOOLICA", responseSecaoDTO.bebidas().get(0).tipoBebida());
//
//        }
    }
}