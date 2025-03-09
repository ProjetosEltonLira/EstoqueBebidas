package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.entities.BebidaEntity;
import com.portifolio.bebidas.core.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.core.entities.HistoricoEntity;
import com.portifolio.bebidas.core.entities.TipoBebidaEntity;
import com.portifolio.bebidas.core.enums.TipoRegistro;
import com.portifolio.bebidas.core.exceptions.BebidaNaoEncontradaException;
import com.portifolio.bebidas.core.repository.BebidaRepository;
import com.portifolio.bebidas.entrypoint.controller.dto.request.InserirBebidaDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BebidaServiceTest {

    @InjectMocks
    BebidaService bebidaService;

    @Mock
    BebidaRepository bebidaRepository;

    @Mock
    TipoBebidaService tipoBebidaService;

    @Mock
    HistoricoService historicoService;

    BebidaEntity bebidaEntity;

    @Nested
    class findById {

        @Test
        @DisplayName("Validar a busca de bebida pelo FindById da Bebida")
        void validarABuscaDaBebidapeloFindByIdBebida() {

            var codigoBebida = 1L;

            bebidaEntity = new BebidaEntity(1L, "Cachaça", null);
            doReturn(Optional.of(bebidaEntity)).when(bebidaRepository).findById(codigoBebida);

            var bebida = bebidaService.findById(codigoBebida);

            assertEquals(1L, bebida.getBebidaId());

        }

        @Test
        @DisplayName("Validar a exception para bebida não encontrada pelo id")
        void validarExceptionAoBuscaDaBebidapeloPeloIdENaoEncontrar() {

            var codigoBebida = 1L;

            doReturn(Optional.empty()).when(bebidaRepository).findById(codigoBebida);

            var exception = assertThrows(BebidaNaoEncontradaException.class,
                    () -> bebidaService.findById(codigoBebida));

            assertEquals("Bebida com o Id " + codigoBebida + " não foi encontrada, para utilizar ela efetue o cadastro da bebida", exception.getMessage(), "Mensagem da exception");

        }
    }

    @Nested
    class findAll {

        @Test
        @DisplayName("Deve retornar uma página de bebidas ordenada corretamente")
        void findAll_DeveRetornarPaginaDeBebidas() {
            // Arrange
            int page = 0;
            int pageSize = 10;
            String orderBy = "asc"; // Testando ordenação ascendente
            Sort.Direction expectedDirection = Sort.Direction.ASC;

            List<BebidaEntity> bebidas = List.of(
                    new BebidaEntity(1L, "Cerveja", new TipoBebidaEntity(1L, "ALCOOLICA")),
                    new BebidaEntity(2L, "VINHO", new TipoBebidaEntity(1L, "ALCOOLICA"))
            );
            Page<BebidaEntity> bebidaPage = new PageImpl<>(bebidas);

            PageRequest expectedPageRequest = PageRequest.of(page, pageSize, expectedDirection, "data_cadastro");

            when(bebidaRepository.findAll(expectedPageRequest)).thenReturn(bebidaPage);

            // Act
            Page<BebidaEntity> result = bebidaService.findAll(page, pageSize, orderBy);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("Cerveja", result.getContent().get(0).getNomeBebida());
            assertEquals("ALCOOLICA", result.getContent().get(0).getTipoBebida().getDescricao());
            assertEquals("VINHO", result.getContent().get(1).getNomeBebida());
            assertEquals("ALCOOLICA", result.getContent().get(1).getTipoBebida().getDescricao());

            verify(bebidaRepository).findAll(expectedPageRequest);
        }


        @Test
        @DisplayName("Deve retornar uma página de bebidas ordenada de forma DESCENDENTE")
        void findAll_DeveRetornarPaginaDeBebidasComOrdemDescendente() {
            // Arrange
            int page = 0;
            int pageSize = 10;
            String orderBy = "DESC";
            Sort.Direction expectedDirection = Sort.Direction.DESC;

            List<BebidaEntity> bebidas = List.of(
                    new BebidaEntity(1L, "Cerveja", new TipoBebidaEntity(1L, "ALCOOLICA")),
                    new BebidaEntity(2L, "VINHO", new TipoBebidaEntity(1L, "ALCOOLICA"))
            );
            Page<BebidaEntity> bebidaPage = new PageImpl<>(bebidas);

            PageRequest expectedPageRequest = PageRequest.of(page, pageSize, expectedDirection, "data_cadastro");

            when(bebidaRepository.findAll(expectedPageRequest)).thenReturn(bebidaPage);

            // Act
            Page<BebidaEntity> result = bebidaService.findAll(page, pageSize, orderBy);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("Cerveja", result.getContent().get(0).getNomeBebida());
            assertEquals("ALCOOLICA", result.getContent().get(0).getTipoBebida().getDescricao());
            assertEquals("VINHO", result.getContent().get(1).getNomeBebida());
            assertEquals("ALCOOLICA", result.getContent().get(1).getTipoBebida().getDescricao());

            verify(bebidaRepository).findAll(expectedPageRequest);
        }
    }

    @Nested
    class inserirBebida {
        @Test
        @DisplayName("Deve inserir uma nova bebida corretamente")
        void inserirBebida_DeveSalvarEBebidaNoRepositorio() {
            // Arrange
            var bebidaDto = new InserirBebidaDto("Cerveja", "ALCOOLICA");
            var tipoBebidaEntity = new TipoBebidaEntity(1L, "ALCOOLICA");
            var bebidaEntity = new BebidaEntity("Cerveja", tipoBebidaEntity);

            when(tipoBebidaService.getTipoBebida(bebidaDto.tipoBebida())).thenReturn(tipoBebidaEntity);
            when(bebidaRepository.save(any(BebidaEntity.class))).thenReturn(bebidaEntity);

            // Act
            var resultado = bebidaService.inserirBebida(bebidaDto);

            // Assert
            assertNotNull(resultado);
            assertEquals("Cerveja", resultado.getNomeBebida());
            assertEquals("ALCOOLICA", resultado.getTipoBebida().getDescricao());

            verify(tipoBebidaService).getTipoBebida("ALCOOLICA");
            verify(bebidaRepository).save(any(BebidaEntity.class));
        }
    }

    @Nested
    class procurarHistoricoDaBebida {

        @Test
        @DisplayName("Deve chamar o serviço de histórico e retornar a página corretamente")
        void procurarHistoricoDaBebida_DeveRetornarPaginaCorretamente() {
            // Arrange
            int page = 0;
            int pageSize = 10;
            String orderBy = "desc";
            Long bebidaId = 1L;

            List<HistoricoEntity> historicoList = List.of(
                    new HistoricoEntity("ELTON", 200.0, TipoRegistro.ENTRADA, new BebidaSecaoEntity()),
                    new HistoricoEntity("GABI", 100.0, TipoRegistro.ENTRADA, new BebidaSecaoEntity())
            );
            Page<HistoricoEntity> historicoPage = new PageImpl<>(historicoList);
            when(historicoService.procurarHistoricoDaBebida(page, pageSize, orderBy, bebidaId)).thenReturn(historicoPage);

            // Act
            Page<HistoricoEntity> result = bebidaService.procurarHistoricoDaBebida(page, pageSize, orderBy, bebidaId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("ELTON", result.getContent().get(0).getNomeSolicitante());
            assertEquals("GABI", result.getContent().get(1).getNomeSolicitante());

            verify(historicoService).procurarHistoricoDaBebida(page, pageSize, orderBy, bebidaId);
        }
    }
}