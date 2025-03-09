package com.portifolio.bebidas.core.entities.mapper;

import com.portifolio.bebidas.core.entities.*;
import com.portifolio.bebidas.core.mapper.BebidaMapper;
import com.portifolio.bebidas.core.mapper.SecaoMapper;
import com.portifolio.bebidas.entrypoint.controller.dto.response.BebidasNaSecaoResponseDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoHistoricoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecaoMapperTest {

    @Mock
    private BebidaMapper bebidaMapper;

    @InjectMocks
    private SecaoMapper secaoMapper = Mappers.getMapper(SecaoMapper.class);

    TipoBebidaEntity tipoBebidaAlcoolica;
    TipoBebidaEntity tipoBebidaSemAlcool;
    BebidaEntity bebida1;
    BebidaEntity bebida2;
    BebidaEntity bebida3;
    SecaoEntity secaoAlcoolica;
    List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();

    @BeforeEach
    void setUp() {

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
        void testToResponseSecaoDto() {

            //Arrange
            var listBebidasNaSecao =
                    List.of(new BebidasNaSecaoResponseDTO(1L, "WISK", "ALCOOLICA", 90.0, LocalDateTime.now()),
                            new BebidasNaSecaoResponseDTO(2L, "BOURBON", "ALCOOLICA", 110.0, LocalDateTime.now()));

            when(bebidaMapper.toBebidasNaSecaoResponseDtoList(secaoAlcoolica.getBebidaSecaoEntities())).thenReturn(listBebidasNaSecao);

            //Act
            ResponseSecaoDTO dto = secaoMapper.toResponseSecaoDto(secaoAlcoolica);
            //Assert
            assertNotNull(dto);
            assertEquals(1L, dto.secaoId());
            assertEquals("ALCOOLICA", dto.tipoSecao());
            assertEquals("SECAO ALCOOLICA", dto.nomeSecao());
            assertEquals(300.0, dto.quantidadeTotal());
            assertEquals(1L, dto.bebidas().get(0).bebidaId());
            assertEquals("WISK", dto.bebidas().get(0).nomeBebida());
            assertEquals("ALCOOLICA", dto.bebidas().get(0).tipoBebida());
            assertEquals(90.0, dto.bebidas().get(0).quantidadeBebida());

            verify(bebidaMapper, times(1)).toBebidasNaSecaoResponseDtoList(any());
        }

        @Test
        void testToResponseSecaoHistoricoDTO() {
            SecaoEntity secaoEntity = new SecaoEntity();
            secaoEntity.setSecaoId(1L);
            secaoEntity.setNomeSecao("Refrigerantes");

            ResponseSecaoHistoricoDTO dto = secaoMapper.toResponseSecaoHistoricoDTO(secaoEntity);

            assertNotNull(dto);
            assertEquals(1L, dto.id());
            assertEquals("Refrigerantes", dto.nome());
        }

    }
}