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
import java.util.stream.Collectors;

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

        var secao = secaoRepository.findById(idSecao)
                .orElseThrow(() -> new SecaoException("Secao informada não existe, por favor, informar o número de uma exceção existe."));

        var quantidadeTotalDeBebidaNaSecao = getQuantidadeTotalDeBebidaNaSecao(secao);
        var quantidadeBebidasASeremInseridas = getQuantidadDeBebidaASeremInseridasNaSecao(dto.bebidas());

        if (TipoRegistro.ENTRADA.getDescricao().equals(dto.tipoRegistro())) {
            var total = quantidadeTotalDeBebidaNaSecao + quantidadeBebidasASeremInseridas;
            validarLimiteDeBebidaNaSecao(secao, total);
        }
//        else {
//            //Registro de SAIDA
//            var quantidadeEmEstoque = quantidadeTotalDeBebidaNaSecao - quantidadeBebidasASeremInseridas;
//            validarDisponibilidadeDeEstoque(quantidadeEmEstoque);
//        }
        //TODO criar logica para não aceitar Tipo de bebidas diferentes do permitido na secao
        var bebidas = instanciarBebidaSecao(secao, dto.bebidas(),dto.tipoRegistro());
        secao.setBebidaSecaoEntities(bebidas);
        return secaoRepository.save(secao);
    }

//    private void validarDisponibilidadeDeEstoque(double quantidadeEmEstoque) {
//        if ( quantidadeEmEstoque < ESTOQUE_ZERADO) {
//            throw new BebidaComValorNegativoNaSecaoException(
//                    "Limite mínimo da seção é 0, não há mais bebidas para serem retiradas.");
//
//        }
//    }

    private void validarLimiteDeBebidaNaSecao(SecaoEntity secao, double total) {
        if (secao.getTipoBebida().isTipo(TipoBebida.ALCOOLICA) && total > LIMITE_BEBIDA_ALCOOLICA)
            throw new SecaoExcedeuLimiteDeCapacidadeException(
                    String.format("A capacidade máxima da seção foi excedida. Limite: %.2f. Total atual: %.2f", LIMITE_BEBIDA_ALCOOLICA, total));


        if (!secao.getTipoBebida().isTipo(TipoBebida.ALCOOLICA) && total > LIMITE_BEBIDA_SEM_ALCOOL)
            throw new SecaoExcedeuLimiteDeCapacidadeException(
                    String.format("A capacidade máxima da seção foi excedida. Limite: %.2f Total atual: %.2f", LIMITE_BEBIDA_SEM_ALCOOL, total));
    }


    private static Double getQuantidadDeBebidaASeremInseridasNaSecao(List<DadosBebidaSecaoDto> bebidas) {
            return bebidas.stream()
                    .map(DadosBebidaSecaoDto::quantidade)
                    .mapToDouble(Double::doubleValue)
                    .sum();
    }

    private static Double getQuantidadeTotalDeBebidaNaSecao(SecaoEntity secao) {
         if (secao.getBebidaSecaoEntities() == null)
             return 0.0;
         else
             return secao.getBebidaSecaoEntities().stream()
                     .map(BebidaSecaoEntity::getQuantidadeBebida)
                     .mapToDouble(Double::doubleValue)
                     .sum();
    }

    private List<BebidaSecaoEntity> instanciarBebidaSecao(SecaoEntity secao, List<DadosBebidaSecaoDto> dto, String tipoRegistro) {

            return dto.stream()
                    .map(bebida -> getBebidaSecao(secao, bebida,tipoRegistro))
                    .collect(Collectors.toList());

    }

    public BebidaSecaoEntity getBebidaSecao(SecaoEntity secaoEntity,
                                            DadosBebidaSecaoDto bebida, String tipoRegistro) {
        var id = new BebidaSecaoId();
        var bebidaSecaoEntity = new BebidaSecaoEntity();
        var bebidaEntity = bebidaService.findById(bebida.id());

        id.setBebida(bebidaEntity);
        id.setSecao(secaoEntity);
        bebidaSecaoEntity.setId(id);

        if (!bebidaEntity.getTipoBebida().equals(secaoEntity.getTipoBebida()))
            throw new TipoDeBebidaNaoPermitidoNaSecaoException(
                    String.format("Bebida do tipo %s somente pode ser inserida em secões que armazenam o mesmo tipo de bebida", bebidaEntity.getTipoBebida().getDescricao()));

        var quantidadeEncontradaBebida = buscarQuantidadeExistenteDaBebidaNaSecao(secaoEntity,bebida);
        if (TipoRegistro.ENTRADA.getDescricao().equals(tipoRegistro))
            bebidaSecaoEntity.setQuantidadeBebida( quantidadeEncontradaBebida + bebida.quantidade());

        else{
            //TipoRegistro.SAIDA
            var quantidadeSobrouEmEstoque = quantidadeEncontradaBebida - bebida.quantidade();
            if (quantidadeSobrouEmEstoque < 0 )
                throw new BebidaComValorNegativoNaSecaoException(String.format("Não foi possível retirar %s da bebida %s, pois somente há %s em estoque.",bebida.quantidade(),bebida.id(),quantidadeEncontradaBebida));

            bebidaSecaoEntity.setQuantidadeBebida(0.0);
        }

        return bebidaSecaoEntity;
    }

    public Double buscarQuantidadeExistenteDaBebidaNaSecao(SecaoEntity secao, DadosBebidaSecaoDto bebida) {

        return secao.getBebidaSecaoEntities()
                .stream()
                .filter(bebidaNaSecao -> bebidaNaSecao.getId().getBebida().getBebidaId().equals(bebida.id()))
                .map(BebidaSecaoEntity::getQuantidadeBebida)
                .reduce(0.0, Double::sum);
    }

    public ResponseSecaoDto procurarSecaoPorId(Long idSecao){
        return secaoMapper.toResponseSecaoDto(findById(idSecao));
    }

    public SecaoEntity findById(Long idSecao) {
        return secaoRepository.findById(idSecao)
                .orElseThrow(() -> new SecaoException("Secao com o Id " + idSecao+ " não encontrada"));
    }
}




