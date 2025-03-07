package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.entities.*;
import com.portifolio.bebidas.core.entities.mapper.BebidaMapper;
import com.portifolio.bebidas.core.entities.mapper.SecaoMapper;
import com.portifolio.bebidas.core.exceptions.*;
import com.portifolio.bebidas.core.repository.SecaoRepository;
import com.portifolio.bebidas.entrypoint.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.entrypoint.controller.dto.request.DadosSecaoDto;
import com.portifolio.bebidas.entrypoint.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.entrypoint.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.entrypoint.controller.dto.response.BebidasNaSecaoResponseDto;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseSecaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SecaoServiceTest {

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private BebidaService bebidaService;

    @Mock
    private TipoBebidaService tipoBebidaService;

    @InjectMocks
    private SecaoService secaoService;

    @Mock
    private SecaoMapper secaoMapper;

    @Mock
    private BebidaMapper bebidaMapper;

    @Captor
    private ArgumentCaptor<SecaoEntity> SecaoEntityAc;

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
        tipoBebidaAlcoolica = new TipoBebidaEntity(1L, "ALCOOLICA");
        tipoBebidaSemAlcool = new TipoBebidaEntity(2L, "SEM ALCOOL");

        bebida1 = new BebidaEntity(1L, "Cachaça", tipoBebidaAlcoolica);
        bebida2 = new BebidaEntity(2L, "SELVAGEM", tipoBebidaAlcoolica);
        bebida3 = new BebidaEntity(3L, "SAQUE", tipoBebidaAlcoolica);

        bebida4 = new BebidaEntity(4L, "CHÁ", tipoBebidaSemAlcool);
        bebida5 = new BebidaEntity(5L, "SUCO", tipoBebidaSemAlcool);

        //Secao com as bebidas do mesmo time
        secaoAlcoolica = new SecaoEntity(1L, "teste", tipoBebidaAlcoolica, null);
        id1 = new BebidaSecaoId(bebida1, secaoAlcoolica);
        id2 = new BebidaSecaoId(bebida2, secaoAlcoolica);
        id3 = new BebidaSecaoId(bebida3, secaoAlcoolica);

        secaoSemAlcool = new SecaoEntity(1L, "teste", tipoBebidaSemAlcool, null);

        listBebidasNaSecao.add(new BebidaSecaoEntity(id1, 90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2, 100.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id3, 110.0));

        secaoAlcoolica.setBebidaSecaoEntities(listBebidasNaSecao);

    }


    @Nested
    class criarSecao {

        @Test
        @DisplayName("Criar a secao com sucesso")
        void DeveCriarUmaSecaoComSucesso() {

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
    }


    @Nested
    class criarSecaoExceptions {

        @ParameterizedTest
        @ValueSource(doubles = {5L, 6L})
        @DisplayName("Deve lançar uma exceção de limite de secoes atingidas")
        void DeveLancarExcecaoQuandoLimiteDeSecoesForAtingido(double quantidadeSecoes) {
            // Arrange
            SecaoDto secaoDto = new SecaoDto(new DadosSecaoDto("Secao 1", "ALCOOLICA"));
            when(secaoRepository.quantidadeSecoesAtivas()).thenReturn((long) quantidadeSecoes);

            // Act & Assert
            var exception = assertThrows(SecaoAtingiuQuantidadeMaximaException.class, () -> secaoService.criarSecao(secaoDto));
            assertEquals("Limite de 5 seções atingido, para cadastrar uma nova seção, excluir uma seção existente.", exception.getMessage());

            verify(secaoRepository, never()).save(any(SecaoEntity.class));
        }
    }

    @Nested
    class cadastrarBebida {

        @Test
        @DisplayName("Deve inserir uma entrada de bebida na secao")
        void DeveCadastrarUmaEntradaDeBebidaNaSecao() {
            // Arrange
            List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();

            var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 50.0);
            var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 70.0);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

            var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

            when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
            when(bebidaService.findById(1L)).thenReturn(bebida1);
            when(bebidaService.findById(2L)).thenReturn(bebida2);
            when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //Act
            var secaoEntity = secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto);

            //Asserts
            assertEquals(bebida1.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getBebidaId(), "A bebida retornada deve ser Cachaça (ID 1)");
            assertEquals(bebida1.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome Cachaça");
            assertEquals(140, secaoEntity.getBebidaSecaoEntities().get(0).getQuantidadeBebida(), "A quantidade de bebida inserida deve ser 140, pois já existia 100 dessa mesma bebida na secao");
            assertEquals(bebida2.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getBebidaId(), "A bebida retornada deve ser SELVAGEM (ID 2)");
            assertEquals(bebida2.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome SELVAGEM");
            assertEquals(170, secaoEntity.getBebidaSecaoEntities().get(1).getQuantidadeBebida(), "A quantidade de bebida inserida deve ser 170, pois já existia 100 da bebida na secao");
            verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
        }

        @Test
        @DisplayName("Deve inserir uma Saida de bebida na secao")
        void DeveCadastrarUmaSaidaDeBebidaNaSecao() {
            // Arrange
            List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
            var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(1L, 50.0);
            var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(2L, 70.0);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);
            var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("SAIDA", "ELTON", dadosBebidaSecaoDtoList);

            when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
            when(bebidaService.findById(1L)).thenReturn(bebida1);
            when(bebidaService.findById(2L)).thenReturn(bebida2);
            when(secaoRepository.save(any(SecaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //act
            var secaoEntity = secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto);

            //asserts
            assertEquals(bebida1.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getBebidaId(), "A bebida retornada deve ser Cachaça (ID 1)");
            assertEquals(bebida1.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(0).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome Cachaça");
            assertEquals(40, secaoEntity.getBebidaSecaoEntities().get(0).getQuantidadeBebida(), "A quantidade de bebida restante na secao deve ser 40");
            assertEquals(bebida2.getBebidaId(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getBebidaId(), "A bebida retornada deve ser SELVAGEM (ID 2)");
            assertEquals(bebida2.getNomeBebida(), secaoEntity.getBebidaSecaoEntities().get(1).getId().getBebida().getNomeBebida(), "A bebida deve ter o nome SELVAGEM");
            assertEquals(30, secaoEntity.getBebidaSecaoEntities().get(1).getQuantidadeBebida(), "A quantidade de bebida restante na secao deve ser 30");
            verify(secaoRepository, times(1)).save(any(SecaoEntity.class));
        }
    }

    @Nested
    class cadastrarBebidaExceptions {

        @Test
        @DisplayName("Validar exception seção não encontrada")
        void testeFindById_DeveLançarSecaoNaoEncontradaException_QuandoIdNaoForEncontrado() {
            // Arrange
            Long idSecao = 1L;
            when(secaoRepository.findById(idSecao)).thenReturn(Optional.empty());

            // Act & Assert
            SecaoNaoEncontradaException exception = assertThrows(SecaoNaoEncontradaException.class,
                    () -> secaoService.findById(idSecao));

            assertEquals("Seção com o Id " + idSecao + " não encontrada", exception.getMessage());
            verify(secaoRepository, times(1)).findById(idSecao);
        }

        @Nested
        class SecaoExcedeuLimiteDeCapacidade {
            @Test
            @DisplayName("Validar exception para tratar inserção excessiva de bebida na secao alcoolica 500 litros")
            void DeveLancarUmaExcecaoQuandoOLimiteDeBebidaAlcoolicaNaSecaoForAtingida() {

                //Arrange
                List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = List.of(new DadosBebidaSecaoDto(1L, 100.0),
                        new DadosBebidaSecaoDto(2L, 101.0));

                var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

                when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));

                //Act & Assert
                var exception = assertThrows(SecaoExcedeuLimiteDeCapacidadeException.class,
                        () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));

                assertEquals("A capacidade máxima da seção foi excedida. Limite: 500,00. Total atual: 501,00", exception.getMessage());

            }

            @Test
            @DisplayName("Validar exception para tratar inserção excessiva de bebida na secao sem alcool 400 litros")
            void DeveLancarUmaExcecaoQuandoOLimiteDeBebidaSemAlcoolicaNaSecaoForAtingida() {

                //Arrange
                var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON",
                        new ArrayList<>(List.of(new DadosBebidaSecaoDto(1L, 200.0),
                                new DadosBebidaSecaoDto(2L, 201.0))));

                when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoSemAlcool));

                //Act & Assert
                var exception = assertThrows(SecaoExcedeuLimiteDeCapacidadeException.class,
                        () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));
                assertEquals("A capacidade máxima da seção foi excedida. Limite: 400,00. Total atual: 401,00", exception.getMessage());
            }





        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -20.0, 1.0})
        @DisplayName("Validar exception para impedir de entrar bebidas com quantidades negativas ou zero")
        void DeveLancarExceptionParaBebidaComValorNegativoOuZeroException(Double quantidadeBebida) {

            // Arrange
            List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = List.of(new DadosBebidaSecaoDto(1L, quantidadeBebida));
            var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

            when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));

            if (quantidadeBebida <= 0.0) {

                // Act & Assert
                var exception = assertThrows(BebidaComValorNegativoOuZeroException.class,
                        () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));

                assertEquals("Quantidade da bebida 1 não pode ser negativa: " + quantidadeBebida, exception.getMessage());
            } else {

                when(bebidaService.findById(1L)).thenReturn(bebida1);
                // Act & Assert
                assertDoesNotThrow(() -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));
            }
        }

        @Test
        @DisplayName("Validar exception para impossibilitar retirar quantidade de bebida inexistentes da secao.")
        void LancarExcecaoBebidaComValorNegativoNaSecaoException() {

            //Arrange
            var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("SAIDA", "ELTON",
                    new ArrayList<>(List.of(new DadosBebidaSecaoDto(1L, 100.0),
                            new DadosBebidaSecaoDto(2L, 110.0))));

            when(secaoRepository.findById(1L)).thenReturn(Optional.ofNullable(secaoAlcoolica));
            when(bebidaService.findById(1L)).thenReturn(bebida1);

            //Act & Assert
            var exception = assertThrows(BebidaComValorNegativoNaSecaoException.class,
                    () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));

            assertEquals("Não foi possível retirar 100,00 da bebida 1, pois somente há 90,00 em estoque.", exception.getMessage());
        }

        @Test
        @DisplayName("Validar cálculo da quantidade total para saída de bebida")
        void deveCalcularQuantidadeTotalParaSaida() {
            // Arrange
            SecaoEntity secaoAlcoolica2 = new SecaoEntity(3L, "teste", tipoBebidaAlcoolica, null);

            var bebidaSecaoId1=  new BebidaSecaoEntity(new BebidaSecaoId(bebida1, secaoAlcoolica2), 250.0);
            var bebidaSecaoId2 = new BebidaSecaoEntity(new BebidaSecaoId(bebida2, secaoAlcoolica2), 250.0);

            List<BebidaSecaoEntity> listaBebidasNaSecao = List.of(bebidaSecaoId1, bebidaSecaoId2);
            secaoAlcoolica2.setBebidaSecaoEntities(listaBebidasNaSecao);

            List<DadosBebidaSecaoDto> bebidas = List.of(new DadosBebidaSecaoDto(1L, 50.0));
            var dto = new InserirBebidaSecaoDto("SAIDA", "ELTON", bebidas);

            when(secaoRepository.findById(1L)).thenReturn(Optional.of(secaoAlcoolica2));
            when(bebidaService.findById(1L)).thenReturn(new BebidaEntity(1L, "Cerveja", tipoBebidaAlcoolica));

            // Act
            secaoService.cadastrarBebidas(1L, dto);

            // Assert
            verify(secaoRepository, times(1)).save(any(SecaoEntity.class)); // Garante que a seção foi salva
        }

        @Test
        @DisplayName("Validar exception para impedir inserir bebida ALCOOLICA/SEM ALCOOL em uma Seção SEM ALCOOL/ALCOOLICA")
        void validar_TipoDeBebidaNaoPermitidoNaSecao() {

            //Arrange
            List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = new ArrayList<>();
            var dadosBebidaSecaoDto1 = new DadosBebidaSecaoDto(4L, 20.0);
            var dadosBebidaSecaoDto2 = new DadosBebidaSecaoDto(5L, 30.0);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto1);
            dadosBebidaSecaoDtoList.add(dadosBebidaSecaoDto2);

            var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

            when(secaoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(secaoAlcoolica));
            when(bebidaService.findById(any())).thenReturn(bebida4);

            //Act & Assert
            assertThrows(TipoDeBebidaNaoPermitidoNaSecaoException.class,
                    () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));
        }
    }


    @Test
    @DisplayName("Testar o método pesquisar por id da secao")
        //@Disabled("Em desenvolvimento - apresentando falha no teste")
    void procurarPorSecao() {

        var bebidas = List.of(new BebidasNaSecaoResponseDto(1L, "teste", "ALCOOLICA", 50.0, LocalDateTime.now()));
        ResponseSecaoDto responseSecaoDto = new ResponseSecaoDto(1L, "Teste Secao", "ALCOOLICA", 300.0, bebidas);

        when(secaoRepository.findById(1L)).thenReturn(Optional.ofNullable(secaoAlcoolica));
        doReturn(responseSecaoDto).when(secaoMapper).toResponseSecaoDto(secaoAlcoolica);

        var response = secaoService.procurarSecaoPorId(secaoAlcoolica.getSecaoId());

        assertEquals(1L, response.secaoId());
        assertEquals("Teste Secao", response.nomeSecao());

    }


    @Test
    @DisplayName("Lancar excecao InvalidDataAccessApiUsageException no cadastro de bebida ")
    void deveLancarUmErroInvalidDataAccessApiUsageException() {

        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = List.of(new DadosBebidaSecaoDto(1L, 20.0));

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

        when(secaoRepository.findById(1L)).thenReturn(Optional.ofNullable(secaoAlcoolica));
        when(bebidaService.findById(any())).thenReturn(bebida1);

        doThrow(new InvalidDataAccessApiUsageException(
                "Não é possível inserir dois registros da mesma bebida em um mesmo pedido, faça mais de uma solicitação " +
                        "para inserir uma bebida duas vezes.")).when(secaoRepository).save(any());

        assertThrows(MultiplasInsercoesDaMesmaBebida.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));

    }

    @Test
    @DisplayName("Lancar excecao de erro genérico no cadastro de bebida ")
    void deveLancarUmErroGenericoNoCadastroDeBebida() {

        List<DadosBebidaSecaoDto> dadosBebidaSecaoDtoList = List.of(new DadosBebidaSecaoDto(1L, 20.0));

        var dadosInserirBebidaSecaoDto = new InserirBebidaSecaoDto("ENTRADA", "ELTON", dadosBebidaSecaoDtoList);

        // Simula a exceção ao tentar salvar a entidade
        doThrow(new RuntimeException("Erro não previsto")).when(secaoRepository).findById(any());

        // Act & Assert
        var exception = assertThrows(RuntimeException.class,
                () -> secaoService.cadastrarBebidas(secaoAlcoolica.getSecaoId(), dadosInserirBebidaSecaoDto));

        assertEquals("Erro não previsto", exception.getMessage(), "A mensagem da exceção deve ser 'Erro não previsto '");

    }


}
