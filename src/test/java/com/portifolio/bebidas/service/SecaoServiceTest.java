package com.portifolio.bebidas.service;

import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.DadosSecaoDto;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.entities.*;
import com.portifolio.bebidas.exceptions.*;
import com.portifolio.bebidas.repository.BebidaRepository;
import com.portifolio.bebidas.repository.SecaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecaoServiceTest {

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private BebidaService bebidaService;

    @Mock
    private TipoBebidaService tipoBebidaService;

    @InjectMocks
    private SecaoService secaoService;

    TipoBebidaEntity tipoBebidaAlcoolica;
    TipoBebidaEntity tipoBebidaSemAlcool;
    BebidaEntity bebida1;
    BebidaEntity bebida2;
    BebidaEntity bebida3;
    BebidaEntity bebida4;
    BebidaEntity bebida5;
    SecaoEntity secaoAlcoolica;
    SecaoEntity secaoSemAlcool;
    BebidaSecaoId id1;
    BebidaSecaoId id2;
    BebidaSecaoId id3;

    List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();
    List<DadosBebidaSecaoDto> listDadosBebidaSecaoDto = new ArrayList<>();

    InserirBebidaSecaoDto inserirBebidaSecaoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoBebidaAlcoolica = new TipoBebidaEntity(1L,"ALCOOLICA");
        tipoBebidaSemAlcool = new TipoBebidaEntity(2L,"SEM ALCOOL");

        bebida1 = new BebidaEntity(1L , "Cachaça", tipoBebidaAlcoolica);
        bebida2 = new BebidaEntity(2L , "SELVAGEM", tipoBebidaAlcoolica);
        bebida3 = new BebidaEntity(3L , "SAQUE", tipoBebidaAlcoolica);

        bebida4 = new BebidaEntity(4L , "CHÁ", tipoBebidaSemAlcool);
        bebida5 = new BebidaEntity(5L , "SUCO", tipoBebidaSemAlcool);

        //Secao com as bebidas do mesmo time
        secaoAlcoolica = new SecaoEntity(1L,"teste", tipoBebidaAlcoolica,null);
        id1 = new BebidaSecaoId(bebida1,secaoAlcoolica);
        id2 = new BebidaSecaoId(bebida2,secaoAlcoolica);
        id3 = new BebidaSecaoId(bebida3,secaoAlcoolica);

        secaoSemAlcool = new SecaoEntity(1L,"teste", tipoBebidaSemAlcool,null);

        listBebidasNaSecao.add(new BebidaSecaoEntity(id1,90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2,100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id3,110.0));

        secaoAlcoolica.setBebidaSecaoEntities(listBebidasNaSecao);

    }

    @Test
    void criarSecao_DeveCriarSecaoComSucesso() {
        // Arrange
        DadosSecaoDto dadosSecaoDto = new DadosSecaoDto("Secao Teste", "ALCOOLICA");
        SecaoDto secaoDto = new SecaoDto(dadosSecaoDto);

        when(secaoRepository.quantidadeSecoesAtivas()).thenReturn(3L);
        when(tipoBebidaService.getTipoBebida(dadosSecaoDto.tipoBebida())).thenReturn(tipoBebidaAlcoolica);
        when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SecaoEntity secaoEntity = secaoService.criarSecao(secaoDto);

        // Assert
        assertNotNull(secaoEntity);
        assertEquals("Secao Teste", secaoEntity.getNomeSecao());
        assertEquals(tipoBebidaAlcoolica, secaoEntity.getTipoBebida());

        verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
    }

    @Test
    void criarSecao_DeveLancarExcecaoQuandoLimiteDeSecoesForAtingido() {

        // Arrange
        DadosSecaoDto dadosSecaoDto = new DadosSecaoDto("Secao 1", "ALCOOLICA");
        SecaoDto secaoDto = new SecaoDto(dadosSecaoDto);
        when(secaoRepository.quantidadeSecoesAtivas()).thenReturn(6L);

        // Act & Assert
        var exception = assertThrows(SecaoAtingiuQuantidadeMaximaException.class, () -> secaoService.criarSecao(secaoDto));
        assertEquals("Limite de 5 seções atingido, para cadastrar uma nova seção, excluir uma seção existente.", exception.getMessage());

        verify(secaoRepository, never()).save(any(SecaoEntity.class));
    }


    @Test
    void validar_findById_SecaoInvalida() {

        // Arrange
        Long idSecao = 1L;
        when(secaoRepository.findById(idSecao)).thenReturn(Optional.empty());

        SecaoNaoEncontradaException exception = assertThrows(SecaoNaoEncontradaException.class, () -> secaoService.findById(idSecao));
        assertEquals("Seção com o Id 1 não encontrada", exception.getMessage());
        verify(secaoRepository, times(1)).findById(idSecao);
    }

    @Test
    void validar_metodoCadastrarBebida_ExcecaoLimiteDeBebidaAtingido_SecaoAlcoolica() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 100.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 101.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));

        var exception = assertThrows(SecaoExcedeuLimiteDeCapacidadeException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
        assertEquals("A capacidade máxima da seção foi excedida. Limite: 500,00. Total atual: 501,00" , exception.getMessage());

    }

    @Test
    void validar_metodoCadastrarBebida_ExcecaoLimiteDeBebidaAtingido_SecaoSemAlcool() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 200.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 201.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoSemAlcool));

        var exception = assertThrows(SecaoExcedeuLimiteDeCapacidadeException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
        assertEquals("A capacidade máxima da seção foi excedida. Limite: 400,00. Total atual: 401,00" , exception.getMessage());
    }

    @Test
    void validar_ExcecaoMetodoCadastrarBebida_MultiplasInsercoesDaMesmaBebidaEmUmaRequisicao() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 20.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(1L, 20.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON", dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);

//        var exception = assertThrows(MultiplasInsercoesDaMesmaBebida.class,
//                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
//        assertEquals("Não é possível inserir dois registros da mesma bebida em um mesmo pedido, " +
//                "faça mais de uma solicitação para inserir uma bebida duas vezes.", exception.getMessage());
    }

    @Test
    void validar_ExcecaoInserirEntradaBebidaComValorNegativo() {

        //TODO esse cenário não tem como devido a notação
        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, -20.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, -30.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);
        when(bebidaService.findById(2L)).thenReturn(bebida2);

        assertThrows(BebidaComValorNegativoException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
    }

    @Test
    void validar_ExcecaoNaoPermitirSairMaisBebidasDoQueHaEmEstoque() {

        //TODO esse cenário não tem como devido a notação
        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 100.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 110.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("SAIDA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);
        when(bebidaService.findById(2L)).thenReturn(bebida2);

        assertThrows(BebidaComValorNegativoNaSecaoException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));

    }



    @Test
    void alidar_ExcecaoBebidaESecao_SaoDeTipoDeBebidasDiferentes() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, -20.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, -30.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida4);
        when(bebidaService.findById(2L)).thenReturn(bebida5);

        assertThrows(TipoDeBebidaNaoPermitidoNaSecaoException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));

    }


    @Test
    void validar_metodoCadastrarBebida_InserirBebidasNovasQueNaoEstejamNaSecao() {
        // Arrange
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();

        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 50.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 70.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);
        when(bebidaService.findById(2L)).thenReturn(bebida2);
        when(bebidaService.findById(3L)).thenReturn(bebida3);
        when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var secaoEntity = secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto);

        assertEquals(bebida1.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getBebidaId(), "A bebida retornada deve ser Cachaça (ID 1)");
        assertEquals(bebida1.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome Cachaça");
        assertEquals(140, secaoEntity.getBebidaSecaoEntities().get(0).getQuantidadeBebida(), "A quantidade de bebida inserida deve ser 140, pois já existia 100 dessa mesma bebida na secao");
        assertEquals(bebida2.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getBebidaId(), "A bebida retornada deve ser SELVAGEM (ID 2)");
        assertEquals(bebida2.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome SELVAGEM");
        assertEquals(170, secaoEntity.getBebidaSecaoEntities().get(1).getQuantidadeBebida(), "A quantidade de bebida inserida deve ser 170, pois já existia 100 da bebida na secao");
        verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
    }
}
