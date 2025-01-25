package com.portifolio.bebidas.service;

import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.enums.TipoRegistro;
import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.DadosSecaoDto;
import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.entities.*;
import com.portifolio.bebidas.exceptions.SecaoException;
import com.portifolio.bebidas.repository.SecaoRepository;
import com.portifolio.bebidas.repository.TipoBebidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecaoServiceTest {

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private BebidaService bebidaService;

    @Mock
    private TipoBebidaRepository tipoBebidaRepository;

    @InjectMocks
    private SecaoService secaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        TipoBebidaEntity tipoBebida = new TipoBebidaEntity(1L,"ALCOOLICA");
        BebidaEntity bebida1 = new BebidaEntity(1L , "Cachaça",tipoBebida);
        BebidaEntity bebida2 = new BebidaEntity(1L , "SELVAGEM",tipoBebida);
        BebidaEntity bebida3 = new BebidaEntity(1L , "SAQUE",tipoBebida);
        SecaoEntity secao1 = new SecaoEntity(1L,"teste", tipoBebida,null);
        BebidaSecaoId id1 = new BebidaSecaoId(bebida1,secao1);
        BebidaSecaoId id2 = new BebidaSecaoId(bebida2,secao1);
        BebidaSecaoId id3 = new BebidaSecaoId(bebida3,secao1);

        List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();
        listBebidasNaSecao.add(new BebidaSecaoEntity(id1,100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2,100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id3,100.0));

        secao1.setBebidaSecaoEntities(listBebidasNaSecao);
    }

//    @Test
//    void criarSecao_DeveCriarSecaoComSucesso() {
//        // Arrange
//        DadosSecaoDto dadosSecaoDto = new DadosSecaoDto("Secao 1", "ALCOOLICA");
//        SecaoDto secaoDto = new SecaoDto(dadosSecaoDto);
//
//        TipoBebidaEntity tipoBebidaEntity = new TipoBebidaEntity();
//        tipoBebidaEntity.setTipoBebidaId(1L);
//
//        when(secaoRepository.quantidadeSecoesAtivas()).thenReturn(3L);
//        when(tipoBebidaRepository.findById(1L)).thenReturn(Optional.of(tipoBebidaEntity));
//        when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        SecaoEntity secaoEntity = secaoService.criarSecao(secaoDto);
//
//        // Assert
//        assertNotNull(secaoEntity);
//        assertEquals("Secao 1", secaoEntity.getNomeSecao());
//        assertEquals(tipoBebidaEntity, secaoEntity.getTipoBebida());
//
//        verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
//    }

    @Test
    void criarSecao_DeveLancarExcecaoQuandoLimiteDeSecoesForAtingido() {
        // Arrange
        when(secaoRepository.quantidadeSecoesAtivas()).thenReturn(6L);
        DadosSecaoDto dadosSecaoDto = new DadosSecaoDto("Secao 1", "ALCOOLICA");
        SecaoDto secaoDto = new SecaoDto(dadosSecaoDto);

        // Act & Assert
        SecaoException exception = assertThrows(SecaoException.class, () -> secaoService.criarSecao(secaoDto));
        assertEquals("Limite de 5 secoes atingida, para continuar uma sessao deve ser excluida.", exception.getMessage());

        verify(secaoRepository, never()).save(any(SecaoEntity.class));
    }

//    @Test
//    void criarSecao_DeveLancarExcecaoQuandoTipoBebidaNaoEncontrado() {
//        // Arrange
//        DadosSecaoDto dadosSecaoDto = new DadosSecaoDto("Secao 1", "INEXISTENTE");
//        SecaoDto secaoDto = new SecaoDto(dadosSecaoDto);
//
//        when(secaoRepository.quantidadeSecoesAtivas()).thenReturn(3L);
//        when(tipoBebidaRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        TipoBebidaException exception = assertThrows(TipoBebidaException.class, () -> secaoService.criarSecao(secaoDto));
//        assertEquals("Tipo de bebida não encontrado!", exception.getMessage());
//
//        verify(secaoRepository, never()).save(any(SecaoEntity.class));
//    }

    @Test
    void validar_metodoGetBebidaSecao() {
        // Arrange

        TipoBebidaEntity tipoBebida = new TipoBebidaEntity(1L,"ALCOOLICA");
        BebidaEntity bebida1 = new BebidaEntity(1L , "Cachaça",tipoBebida);
        BebidaEntity bebida2 = new BebidaEntity(2L , "SELVAGEM",tipoBebida);
        BebidaEntity bebida3 = new BebidaEntity(3L , "SAQUE",tipoBebida);
        SecaoEntity secao1 = new SecaoEntity(1L,"teste", tipoBebida,null);
        BebidaSecaoId id1 = new BebidaSecaoId(bebida1,secao1);
        BebidaSecaoId id2 = new BebidaSecaoId(bebida2,secao1);
        BebidaSecaoId id3 = new BebidaSecaoId(bebida3,secao1);

        List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();
        listBebidasNaSecao.add(new BebidaSecaoEntity(id1,90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2,100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id3,110.0));

        secao1.setBebidaSecaoEntities(listBebidasNaSecao);

        List<DadosBebidaSecaoDto> listDadosBebidaSecaoDto = new ArrayList<>();
        listDadosBebidaSecaoDto.add(new DadosBebidaSecaoDto(1L,50.0));
        listDadosBebidaSecaoDto.add(new DadosBebidaSecaoDto(2L,50.0));

        InserirBebidaSecaoDto inserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA",listDadosBebidaSecaoDto);


        when(bebidaService.findById(anyLong())).thenReturn(bebida2);

        var secaoResult = secaoService.cadastrarBebidas(secao1.getSecaoId(),inserirBebidaSecaoDto);

//        assertEquals(bebida2, secaoResult.getBebidaSecaoEntities()tId().getBebida(), "A bebida retornada deve ser SELVAGEM (ID 2)");
//        assertEquals(secao1, secaoResult.getId().getSecao(), "A seção deve ser a configurada (ID 1)");
//        assertEquals(150.0, secaoResult.getQuantidadeBebida(), "A quantidade de bebida deve ser 150.0");
    }


    @Test
    void validar_metodoGetBebidaSecao_InserirBebidaNovaQueNaoEstejaNaSecao() {
        // Arrange
        TipoBebidaEntity tipoBebida = new TipoBebidaEntity(1L, "ALCOOLICA");
        BebidaEntity bebida1 = new BebidaEntity(1L, "Cachaça", tipoBebida);
        BebidaEntity bebida2 = new BebidaEntity(2L, "SELVAGEM", tipoBebida);
        BebidaEntity bebida3 = new BebidaEntity(3L, "SAQUE", tipoBebida);
        BebidaEntity bebida4 = new BebidaEntity(3L, "Martini", tipoBebida);
        SecaoEntity secao1 = new SecaoEntity(1L, "teste", tipoBebida, null);
        BebidaSecaoId id1 = new BebidaSecaoId(bebida1, secao1);
        BebidaSecaoId id2 = new BebidaSecaoId(bebida2, secao1);
        BebidaSecaoId id3 = new BebidaSecaoId(bebida3, secao1);

        List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();
        listBebidasNaSecao.add(new BebidaSecaoEntity(id1, 90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2, 100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id3, 110.0));

        secao1.setBebidaSecaoEntities(listBebidasNaSecao);
        DadosBebidaSecaoDto dadosBebidaSecaoDto = new DadosBebidaSecaoDto(4L, 50.0);

        // Mock do método bebidaService.findById
        when(bebidaService.findById(anyLong())).thenReturn(bebida2);

//        BebidaSecaoEntity bebidaSecaoResult = secaoService.getBebidaSecao(secao1,dadosBebidaSecaoDto, TipoRegistro.ENTRADA.getDescricao());
//
//        assertEquals(bebida2, bebidaSecaoResult.getId().getBebida(), "A bebida retornada deve ser SELVAGEM (ID 4)");
//        assertEquals(secao1, bebidaSecaoResult.getId().getSecao(), "A seção deve ser a configurada (ID 1)");
//        assertEquals(50.0, bebidaSecaoResult.getQuantidadeBebida(), "A quantidade de bebida deve ser 50.0");
//    }
    }

}
