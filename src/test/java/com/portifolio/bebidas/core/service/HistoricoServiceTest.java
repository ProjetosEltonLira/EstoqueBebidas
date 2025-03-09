package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.core.entities.HistoricoEntity;
import com.portifolio.bebidas.core.enums.TipoRegistro;
import com.portifolio.bebidas.core.repository.HistoricoRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricoServiceTest {

    @Mock
    private HistoricoRepository historicoRepository;

    @InjectMocks
    private HistoricoService historicoService;

    @Nested
    class procurarHistoricoDaBebida {

        @Test
        @DisplayName("Deve retornar uma página com o histórico da bebida corretamente ordenada")
        void procurarHistoricoDaBebida_DeveRetornarPaginaCorretamente() {
            // Arrange
            int page = 0;
            int pageSize = 10;
            String orderBy = "asc";
            Long bebidaId = 1L;
            Sort.Direction expectedDirection = Sort.Direction.ASC;

            List<HistoricoEntity> historicoList = List.of(
                    new HistoricoEntity("ELTON", 200.0, TipoRegistro.ENTRADA, new BebidaSecaoEntity()),
                    new HistoricoEntity("GABI", 100.0, TipoRegistro.ENTRADA, new BebidaSecaoEntity())
            );
            Page<HistoricoEntity> historicoPage = new PageImpl<>(historicoList);

            PageRequest expectedPageRequest = PageRequest.of(page, pageSize, expectedDirection, "data_registro");

            when(historicoRepository.findAllHistoricoBebida(bebidaId, expectedPageRequest)).thenReturn(historicoPage);

            // Act
            Page<HistoricoEntity> result = historicoService.procurarHistoricoDaBebida(page, pageSize, orderBy, bebidaId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("ELTON", result.getContent().get(0).getNomeSolicitante());
            assertEquals("GABI", result.getContent().get(1).getNomeSolicitante());

            verify(historicoRepository).findAllHistoricoBebida(bebidaId, expectedPageRequest);
        }
    }
}