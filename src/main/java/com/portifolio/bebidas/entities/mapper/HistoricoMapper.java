package com.portifolio.bebidas.entities.mapper;

import com.portifolio.bebidas.entities.HistoricoEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

//TODO configurar esse mapper
@Configuration
public interface HistoricoMapper {

    //@Mapping(target = "tipoBebida", source = "tipoBebida")
    //ResponseHistoricoDTO toResponseHistoricoDto(Page<HistoricoEntity> historicoEntityPage)




//    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapTipoBebida")
//    BebidaEntity toBebidaEntity(DadosBebidaDto dadosBebidaDto);
//
//    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapTipoBebida")
//    List<BebidaEntity> toBebidaEntityList(List<DadosBebidaDto> dadosBebidaDtoList);
//
//    @Mapping(target = "id", source = "bebidaId")
//    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapDescricaoTipoBebida")
//    ResponseFindAllBebidasDto toResponseFindAllBebidasDto(BebidaEntity bebidaEntity);
//
//    @Mapping(target = "id", source = "bebidaId")
//    List<ResponseFindAllBebidasDto> toResponseFindAllBebidasDtoList(List<BebidaEntity> bebidaEntity);
//
//    @Named("mapTipoBebida")
//    default TipoBebidaEntity mapTipoBebida(String descricaoTipoBebida) {
//        Integer codigoTipoBebida = TipoBebida.getCodigoByDescricao(descricaoTipoBebida);
//        return new TipoBebidaEntity(Long.valueOf(codigoTipoBebida), descricaoTipoBebida);
//    }
//
//    @Mapping(target = "bebidaId", source = "id.bebida.bebidaId")
//    @Mapping(target = "nomeBebida", source = "id.bebida.nomeBebida")
//    @Mapping(target = "tipoBebida", source = "id.bebida.tipoBebida", qualifiedByName = "mapDescricaoTipoBebida")
//    @Mapping(target = "quantidadeBebida", source = "quantidadeBebida")
//    @Mapping(target = "dataCadastro", source = "dataCadastro")
//    BebidasNaSecaoResponseDto toBebidasNaSecaoResponseDto(BebidaSecaoEntity bebidaSecaoEntity);
//
//    List<BebidasNaSecaoResponseDto> toBebidasNaSecaoResponseDtoList(List<BebidaSecaoEntity> bebidaSecaoEntities);

}