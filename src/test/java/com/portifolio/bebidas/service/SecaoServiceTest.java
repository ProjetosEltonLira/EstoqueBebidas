package com.portifolio.bebidas.service;

import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.DadosSecaoDto;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.controller.dto.response.BebidasNaSecaoResponseDto;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;
import com.portifolio.bebidas.entities.*;
import com.portifolio.bebidas.entities.mapper.BebidaMapper;
import com.portifolio.bebidas.entities.mapper.SecaoMapper;
import com.portifolio.bebidas.exceptions.*;
import com.portifolio.bebidas.repository.SecaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @InjectMocks
    private final SecaoMapper secaoMapper = Mappers.getMapper(SecaoMapper.class);

    @Mock
    private BebidaMapper bebidaMapper;

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

    @Disabled
    @ParameterizedTest
    @ValueSource(doubles = { 0.0, 1.5, 2.5 })
    void testaValoresDeQuantidade(double quantidade) {
        // Lógica do teste usando o valor de "quantidade"
    }

    @Test
    @DisplayName("Teste 1 - Criar a secao com sucesso")
    void Teste_1_criarSecao_DeveCriarSecaoComSucesso() {
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
    @DisplayName("Teste 2 - Ao criar a secao deve lançar uma exceção de limite de secoes atingidas")
    void Teste_2_CriarSecao_DeveLancarExcecaoQuandoLimiteDeSecoesForAtingido() {

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
    @DisplayName("Teste 3 - validar exceção de Secao não encontrada")
    void teste_3_validar_findById_SecaoInvalida() {

        // Arrange
        Long idSecao = 1L;
        when(secaoRepository.findById(idSecao)).thenReturn(Optional.empty());

        SecaoNaoEncontradaException exception = assertThrows(SecaoNaoEncontradaException.class, () -> secaoService.findById(idSecao));
        assertEquals("Seção com o Id 1 não encontrada", exception.getMessage());
        verify(secaoRepository, times(1)).findById(idSecao);
    }

    @Test
    @DisplayName("Teste 4 - validar inserção de bebidas na secao")
    void Validar_metodoCadastrarBebida_InserirBebidasNovasQueNaoEstejamNaSecao() {
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

    @Test
    @DisplayName("Teste 5 - validar SAIDA de bebidas das seção")
    void Validar_metodoCadastrarBebida_SaidaDeBebidas() {
        // Arrange
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 50.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 70.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);
        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("SAIDA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);
        when(bebidaService.findById(2L)).thenReturn(bebida2);
        when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        var secaoEntity = secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto);

        //asserts
        assertEquals(bebida1.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getBebidaId(), "A bebida retornada deve ser Cachaça (ID 1)");
        assertEquals(bebida1.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome Cachaça");
        assertEquals(40, secaoEntity.getBebidaSecaoEntities().get(0).getQuantidadeBebida(), "A quantidade de bebida restante na secao deve ser 40");
        assertEquals(bebida2.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getBebidaId(), "A bebida retornada deve ser SELVAGEM (ID 2)");
        assertEquals(bebida2.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome SELVAGEM");
        assertEquals(30, secaoEntity.getBebidaSecaoEntities().get(1).getQuantidadeBebida(), "A quantidade de bebida restante na secao deve ser 30");
        verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
    }

    @Test
    @DisplayName("Teste 6 - validar exception para tratar inserção excessiva de bebida na secao alcoolica 500 litros")
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
    @DisplayName("Teste 7 - validar exception para tratar inserção excessiva de bebida na secao sem alcool 400 litros")
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
    @DisplayName("Teste 8 - validar exception para tratar erros relacionados ao save da entidade")
    void deveLancarMultiplasInsercoesDaMesmaBebidaQuandoInvalidDataAccessApiUsageExceptionAcontecer() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 20.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON", dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(1L)).thenReturn(bebida1);

        // Simula a exceção ao tentar salvar a entidade
        when(secaoRepository.save(any(SecaoEntity.class)))
                .thenThrow(new InvalidDataAccessApiUsageException("Erro ao acessar o banco de dados"));

        assertThrows(MultiplasInsercoesDaMesmaBebida.class,
            () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
    }

    @Test
    @DisplayName("Teste 9 - Validar exception para impedir de entrar bebidas com quantidades negativas ")
    void validar_BebidaComValorNegativoException() {

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
    @DisplayName("Teste 10 - Validar exception para impedir retirar quantidade de bebida inexistentes da secao.")
    void validar_BebidaComValorNegativoNaSecaoException() {

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
    @DisplayName("Teste 11 - Validar exception para impedir inserir bebida de um TIPO em uma Seção de outro tipo.")
    void validar_TipoDeBebidaNaoPermitidoNaSecao() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(4L, 20.0);
        var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(5L, 30.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON",dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(any())).thenReturn(bebida4);

        assertThrows(TipoDeBebidaNaoPermitidoNaSecaoException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
    }

    @Test
    @DisplayName("Teste 12 - Testar o secaoMapper.")
    void deveMapearSecaoEntityParaResponseSecaoDtoCorretamente() {

        // Criando um mock de BebidasNaSecaoResponseDto
        BebidasNaSecaoResponseDto bebidaResponseDto = new BebidasNaSecaoResponseDto(1L,"teste","ALCOOLICA",50.0, LocalDateTime.now());

        // Configurando o comportamento do BebidaMapper
        when(bebidaMapper.toBebidasNaSecaoResponseDtoList(secaoAlcoolica.getBebidaSecaoEntities()))
                .thenReturn(Collections.singletonList(bebidaResponseDto));

        // Chamando o mapper
        ResponseSecaoDto responseDto = secaoMapper.toResponseSecaoDto(secaoAlcoolica);

        // Verificando os mapeamentos
        assertNotNull(responseDto);
        assertEquals(responseDto.secaoId(),secaoAlcoolica.getSecaoId());
        assertEquals(responseDto.nomeSecao(),secaoAlcoolica.getNomeSecao());
        assertEquals(responseDto.tipoSecao(),secaoAlcoolica.getTipoBebida().getDescricao());
        assertEquals(responseDto.quantidadeTotal(),300.0);
    }

    @Test
    @DisplayName("Teste 12 - Testar o método pesquisar por id da secao")
    @Disabled ("Em desenvolvimento - apresentando falha no teste")
    void procurarPorSecao() {

        // Criando um mock de BebidasNaSecaoResponseDto
        BebidasNaSecaoResponseDto bebidaResponseDto = new BebidasNaSecaoResponseDto(1L, "teste", "ALCOOLICA", 50.0, LocalDateTime.now());
        List<BebidasNaSecaoResponseDto> bebidas = new ArrayList<>();
        bebidas.add(bebidaResponseDto);
        ResponseSecaoDto responseSecaoDto = new ResponseSecaoDto(1L, "teste", "ALCOOLICA", 300.0, bebidas);

        List<BebidasNaSecaoResponseDto> bebidas2 = new ArrayList<>();
        bebidas.add(new BebidasNaSecaoResponseDto(1L, "teste", "ALCOOLICA", 50.0, LocalDateTime.now()));

        when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(secaoMapper.toResponseSecaoDto(secaoAlcoolica)).thenReturn(responseSecaoDto);

        // Chamando o serviço
        // var responseDto = secaoService.cadastrarBebidas();

        // Verificando os mapeamentos
        //        assertNotNull(responseDto);
        //        assertEquals(responseDto.secaoId(), secaoAlcoolica.getSecaoId());
        //        assertEquals(responseDto.nomeSecao(), secaoAlcoolica.getNomeSecao());
        //        assertEquals(responseDto.tipoSecao(), secaoAlcoolica.getTipoBebida().getDescricao());
        //        assertEquals(responseDto.quantidadeTotal(), 300.0);
    }


    @Test
    @DisplayName("Teste 13 - validar Erro genérico na aplicação")
    void deveLancarUmErroGenerico() {

        //Secao1 alcoolica
        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
        var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 20.0);
        dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA","ELTON", dadosBebidaSecaoDtoList);

        // Simula a exceção ao tentar salvar a entidade
        when(secaoRepository.findById(any())).thenThrow(new RuntimeException("Erro genérico"));

        assertThrows(RuntimeException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(),dadosInserirBebidaSecaoDto));
    }


}
