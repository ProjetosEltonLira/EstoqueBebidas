package com.portifolio.bebidas.service;

import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;
import com.portifolio.bebidas.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.entities.BebidaSecaoId;
import com.portifolio.bebidas.entities.HistoricoEntity;
import com.portifolio.bebidas.entities.SecaoEntity;
import com.portifolio.bebidas.entities.mapper.SecaoMapper;
import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.enums.TipoRegistro;
import com.portifolio.bebidas.exceptions.*;
import com.portifolio.bebidas.repository.SecaoRepository;
import com.portifolio.bebidas.utils.JsonUtil;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SecaoService {

    private static final Logger logger = LoggerFactory.getLogger(SecaoService.class);

    private final BebidaService bebidaService;
    private final TipoBebidaService tipoBebidaService;
    private final SecaoRepository secaoRepository;
    private final SecaoMapper secaoMapper;

    public SecaoService(BebidaService bebidaService, TipoBebidaService tipoBebidaService, SecaoRepository secaoRepository, SecaoMapper secaoMapper) {
        this.bebidaService = bebidaService;
        this.tipoBebidaService = tipoBebidaService;
        this.secaoRepository = secaoRepository;
        this.secaoMapper = secaoMapper;

    }

    public SecaoEntity criarSecao(@Valid SecaoDto dto) {
        logger.info("Iniciando criação da seção: {}", JsonUtil.toJson(dto));

        long quantidadeSecoes = secaoRepository.quantidadeSecoesAtivas();
        int LIMITE_DE_SECOES = 5;
        if (quantidadeSecoes >= LIMITE_DE_SECOES) {
            logger.warn("Tentativa de criar uma nova seção falhou. Limite de {} seções ativas atingido.", LIMITE_DE_SECOES);
            throw new SecaoAtingiuQuantidadeMaximaException("Limite de 5 seções atingido, para cadastrar uma nova seção, excluir uma seção existente.");
        }

        var tipoBebida = tipoBebidaService.getTipoBebida(dto.secao().tipoBebida());
        SecaoEntity secaoEntity = new SecaoEntity(dto.secao().nome(), tipoBebida);

        return secaoRepository.save(secaoEntity);
    }

    @Transient
    public SecaoEntity cadastrarBebidas(Long idSecao, InserirBebidaSecaoDto dto) {

        try {
            logger.info("Request recebida: {}", JsonUtil.toJson(dto));
            var secao = processarCadastro(idSecao, dto);

            return secaoRepository.save(secao);

        } catch (InvalidDataAccessApiUsageException exception) {
            logger.warn("Multiplas insercoes da mesma bebida", exception);
            throw new MultiplasInsercoesDaMesmaBebida("Não é possível inserir dois registros da mesma bebida em um mesmo pedido, faça mais de uma solicitação para inserir uma bebida duas vezes.");

        }  catch (SecaoExcedeuLimiteDeCapacidadeException | TipoDeBebidaNaoPermitidoNaSecaoException | BebidaComValorNegativoNaSecaoException
                  | BebidaComValorNegativoException | SecaoNaoEncontradaException | BebidaNaoEncontradaException exception) {
            logger.warn("Erro de validação: {}", exception.getMessage(), exception);
            throw exception;

        }catch (RuntimeException exception) {
            logger.error("Erro não previsto:", exception);
            throw new RuntimeException("Erro não previsto ",exception);
        }
    }

    private SecaoEntity processarCadastro(Long idSecao, InserirBebidaSecaoDto dto) {
        var secao = findById(idSecao);

        validarNaoPermiteEntradaValoresNegativos(dto.bebidas());
        double quantidadeTotal = calcularQuantidadeTotal(secao, dto);
        validarQuantidadePermitida(secao, quantidadeTotal);

        var bebidas = criarBebidasParaSecao(secao, dto);
        secao.setBebidaSecaoEntities(bebidas);
        return secao;
    }

    private void validarNaoPermiteEntradaValoresNegativos(List<DadosBebidaSecaoDto> bebidas) {
        bebidas.stream()
                .filter(bebida -> bebida.quantidade() < 0)
                .findAny()
                .ifPresent(b -> {
                    throw new BebidaComValorNegativoException("Quantidade da bebida " + b.id() + " não pode ser negativa: "+ b.quantidade());
                });
    }

    private double calcularQuantidadeTotal(SecaoEntity secao, InserirBebidaSecaoDto dto) {
        var quantidadeExistente = calcularQuantidadeTotalNaSecao(secao);
        var quantidadeASerInserida = calcularQuantidadeASerInserida(dto.bebidas());
        return quantidadeExistente + (TipoRegistro.ENTRADA.getDescricao().equals(dto.tipoRegistro()) ? quantidadeASerInserida : 0);
    }

    private void validarQuantidadePermitida(SecaoEntity secao, double quantidadeTotal) {
        double LIMITE_BEBIDA_ALCOOLICA = 500.0;
        double LIMITE_BEBIDA_SEM_ALCOOL = 400.0;
        double limite = secao.getTipoBebida().isTipo(TipoBebida.ALCOOLICA) ? LIMITE_BEBIDA_ALCOOLICA : LIMITE_BEBIDA_SEM_ALCOOL;
        if (quantidadeTotal > limite) {
            throw new SecaoExcedeuLimiteDeCapacidadeException(String.format(
                    "A capacidade máxima da seção foi excedida. Limite: %.2f. Total atual: %.2f", limite, quantidadeTotal));
        }
    }

    private List<BebidaSecaoEntity> criarBebidasParaSecao(SecaoEntity secao, InserirBebidaSecaoDto dadosPedido) {
        return dadosPedido.bebidas().stream()
                .map(bebida -> criarBebida(secao, bebida, dadosPedido))
                .collect(Collectors.toList());
    }

    private BebidaSecaoEntity criarBebida(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto, InserirBebidaSecaoDto dadosPedido) {
        validarTipoDeBebida(secao, bebidaDto);

        var bebidaSecao = inicializarBebidaSecao(secao, bebidaDto);
        atualizarQuantidadeBebida(secao, bebidaSecao, bebidaDto, dadosPedido);
        atualizarHistoricoBebida(secao, bebidaSecao,bebidaDto,dadosPedido);

        return bebidaSecao;
    }

    private void atualizarHistoricoBebida(SecaoEntity secao, BebidaSecaoEntity bebidaSecao, DadosBebidaSecaoDto bebidaDto, InserirBebidaSecaoDto dadosPedido) {

        var tipoRegistro = TipoRegistro.valueOf(dadosPedido.tipoRegistro().toUpperCase());
        var historico = new HistoricoEntity(dadosPedido.nomeSolicitante(),bebidaDto.quantidade(),tipoRegistro,bebidaSecao);
        bebidaSecao.getHistorico().add(historico);
    }

    private void validarTipoDeBebida(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto) {
        var bebida = bebidaService.findById(bebidaDto.id());
        if (!bebida.getTipoBebida().equals(secao.getTipoBebida())) {
            throw new TipoDeBebidaNaoPermitidoNaSecaoException(String.format(
                    "A bebida %s do tipo %s somente pode ser inserida em seções que armazenam o mesmo tipo de bebida",bebida.getBebidaId(),
                    bebida.getTipoBebida().getDescricao()));
        }
    }

    private BebidaSecaoEntity inicializarBebidaSecao(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto) {
        var bebida = bebidaService.findById(bebidaDto.id());

        var id = new BebidaSecaoId();
        id.setBebida(bebida);
        id.setSecao(secao);

        var bebidaSecao = new BebidaSecaoEntity();
        bebidaSecao.setId(id);

        return bebidaSecao;
    }

    private void atualizarQuantidadeBebida(SecaoEntity secao, BebidaSecaoEntity bebidaSecao,
                                           DadosBebidaSecaoDto bebidaDto, InserirBebidaSecaoDto dadosPedido) {
        double quantidadeExistente = buscarQuantidadeExistente(secao, bebidaDto);

        if (TipoRegistro.ENTRADA.getDescricao().equals(dadosPedido.tipoRegistro())) {
            bebidaSecao.setQuantidadeBebida(quantidadeExistente + bebidaDto.quantidade());

        } else if (TipoRegistro.SAIDA.getDescricao().equals(dadosPedido.tipoRegistro())) {
            double novaQuantidade = quantidadeExistente - bebidaDto.quantidade();
            if (novaQuantidade < 0) {
                throw new BebidaComValorNegativoNaSecaoException(String.format(
                        "Não foi possível retirar %.2f da bebida %s, pois somente há %.2f em estoque.",
                        bebidaDto.quantidade(), bebidaDto.id(), quantidadeExistente));
            }
            bebidaSecao.setQuantidadeBebida(novaQuantidade);
        }
    }

    protected double calcularQuantidadeTotalNaSecao(SecaoEntity secao) {
        return secao.getBebidaSecaoEntities() == null ? 0.0 :
                secao.getBebidaSecaoEntities().stream()
                        .mapToDouble(BebidaSecaoEntity::getQuantidadeBebida)
                        .sum();
    }

    private double calcularQuantidadeASerInserida(List<DadosBebidaSecaoDto> bebidas) {
        return bebidas.stream()
                .mapToDouble(DadosBebidaSecaoDto::quantidade)
                .sum();
    }

    private double buscarQuantidadeExistente(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto) {
        return secao.getBebidaSecaoEntities().stream()
                .filter(b -> b.getId().getBebida().getBebidaId().equals(bebidaDto.id()))
                .mapToDouble(BebidaSecaoEntity::getQuantidadeBebida)
                .sum();
    }

    public ResponseSecaoDto procurarSecaoPorId(Long idSecao) {
        return secaoMapper.toResponseSecaoDto(findById(idSecao));
    }

    public SecaoEntity findById(Long idSecao) {
        return secaoRepository.findById(idSecao)
                .orElseThrow(() -> new SecaoNaoEncontradaException("Seção com o Id " + idSecao + " não encontrada"));
    }
}




