package com.portifolio.bebidas.service;


import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.enums.TipoRegistro;
import com.portifolio.bebidas.controller.dto.request.DadosBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.InserirBebidaSecaoDto;
import com.portifolio.bebidas.controller.dto.request.SecaoDto;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;
import com.portifolio.bebidas.entities.*;
import com.portifolio.bebidas.entities.mapper.SecaoMapper;
import com.portifolio.bebidas.exceptions.BebidaComValorNegativoNaSecaoException;
import com.portifolio.bebidas.exceptions.SecaoExcedeuLimiteDeCapacidadeException;
import com.portifolio.bebidas.exceptions.TipoDeBebidaNaoPermitidoNaSecaoException;
import com.portifolio.bebidas.exceptions.SecaoException;
import com.portifolio.bebidas.repository.SecaoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecaoService {


    private static final Logger logger = LoggerFactory.getLogger(SecaoService.class);

    private final Double LIMITE_BEBIDA_ALCOOLICA = 500.0;
    private final Double LIMITE_BEBIDA_SEM_ALCOOL = 400.0;
    private final Double ESTOQUE_ZERADO = 0.0;
    private final int    LIMITE_DE_SECOES = 5;

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

        long quantidadeSecoes = secaoRepository.quantidadeSecoesAtivas();
        if (quantidadeSecoes > LIMITE_DE_SECOES){
            throw new SecaoException("Limite de 5 secoes atingida, para continuar uma sessao deve ser excluida.");
        }

        try {
            var tipoBebida = tipoBebidaService.getTipoBebida(dto.secao().tipoBebida());
            SecaoEntity secaoEntity = new SecaoEntity(dto.secao().nome(),tipoBebida);
            return secaoRepository.save(secaoEntity);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public SecaoEntity cadastrarBebidas(Long idSecao, InserirBebidaSecaoDto dto) {
        var secao = findById(idSecao);

        double quantidadeTotal = calcularQuantidadeTotal(secao, dto);
        validarQuantidadePermitida(secao, quantidadeTotal);

        var bebidas = criarBebidasParaSecao(secao, dto);
        secao.setBebidaSecaoEntities(bebidas);

        return secaoRepository.save(secao);
    }

    private double calcularQuantidadeTotal(SecaoEntity secao, InserirBebidaSecaoDto dto) {
        var quantidadeExistente = calcularQuantidadeTotalNaSecao(secao);
        var quantidadeASerInserida = calcularQuantidadeASerInserida(dto.bebidas());
        return quantidadeExistente + (TipoRegistro.ENTRADA.getDescricao().equals(dto.tipoRegistro()) ? quantidadeASerInserida : 0);
    }

    private void validarQuantidadePermitida(SecaoEntity secao, double quantidadeTotal) {
        double limite = secao.getTipoBebida().isTipo(TipoBebida.ALCOOLICA) ? LIMITE_BEBIDA_ALCOOLICA : LIMITE_BEBIDA_SEM_ALCOOL;
        if (quantidadeTotal > limite) {
            throw new SecaoExcedeuLimiteDeCapacidadeException(String.format(
                    "A capacidade máxima da seção foi excedida. Limite: %.2f. Total atual: %.2f", limite, quantidadeTotal));
        }
    }

    private List<BebidaSecaoEntity> criarBebidasParaSecao(SecaoEntity secao, InserirBebidaSecaoDto dto) {
        return dto.bebidas().stream()
                .map(bebida -> criarBebida(secao, bebida, dto.tipoRegistro()))
                .toList();
    }

    private BebidaSecaoEntity criarBebida(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto, String tipoRegistro) {
        validarTipoDeBebida(secao, bebidaDto);

        var bebidaSecao = inicializarBebidaSecao(secao, bebidaDto);
        atualizarQuantidadeBebida(secao, bebidaSecao, bebidaDto, tipoRegistro);

        return bebidaSecao;
    }

    private void validarTipoDeBebida(SecaoEntity secao, DadosBebidaSecaoDto bebidaDto) {
        var bebida = bebidaService.findById(bebidaDto.id());
        if (!bebida.getTipoBebida().equals(secao.getTipoBebida())) {
            throw new TipoDeBebidaNaoPermitidoNaSecaoException(String.format(
                    "Bebida do tipo %s somente pode ser inserida em seções que armazenam o mesmo tipo de bebida",
                    bebida.getTipoBebida().getDescricao()
            ));
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
                                           DadosBebidaSecaoDto bebidaDto, String tipoRegistro) {
        double quantidadeExistente = buscarQuantidadeExistente(secao, bebidaDto);

        if (TipoRegistro.ENTRADA.getDescricao().equals(tipoRegistro)) {
            bebidaSecao.setQuantidadeBebida(quantidadeExistente + bebidaDto.quantidade());
        } else if (TipoRegistro.SAIDA.getDescricao().equals(tipoRegistro)) {
            double novaQuantidade = quantidadeExistente - bebidaDto.quantidade();
            if (novaQuantidade < 0) {
                throw new BebidaComValorNegativoNaSecaoException(String.format(
                        "Não foi possível retirar %.2f da bebida %s, pois somente há %.2f em estoque.",
                        bebidaDto.quantidade(), bebidaDto.id(), quantidadeExistente));
            }
            bebidaSecao.setQuantidadeBebida(novaQuantidade);
        }
    }

    private double calcularQuantidadeTotalNaSecao(SecaoEntity secao) {
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
                .orElseThrow(() -> new SecaoException("Seção com o Id " + idSecao + " não encontrada"));
    }
}




