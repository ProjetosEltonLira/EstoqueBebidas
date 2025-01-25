package com.portifolio.bebidas.entities.mapper;


import com.portifolio.bebidas.enums.TipoBebida;
import com.portifolio.bebidas.controller.dto.request.DadosBebidaDto;
import com.portifolio.bebidas.controller.dto.response.BebidasNaSecaoResponseDto;
import com.portifolio.bebidas.controller.dto.response.ResponseFindAllBebidasDto;
import com.portifolio.bebidas.entities.BebidaEntity;
import com.portifolio.bebidas.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.entities.TipoBebidaEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Mapper(componentModel = "spring",uses = {TipoBebidaMapper.class})
public interface BebidaMapper {

    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapTipoBebida")
    BebidaEntity toBebidaEntity(DadosBebidaDto dadosBebidaDto);

    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapTipoBebida")
    List<BebidaEntity> toBebidaEntityList(List<DadosBebidaDto> dadosBebidaDtoList);

    @Mapping(target = "id", source = "bebidaId")
    @Mapping(target = "tipoBebida", source = "tipoBebida", qualifiedByName = "mapDescricaoTipoBebida")
    ResponseFindAllBebidasDto toResponseFindAllBebidasDto(BebidaEntity bebidaEntity);

    @Mapping(target = "id", source = "bebidaId")
    List<ResponseFindAllBebidasDto> toResponseFindAllBebidasDtoList(List<BebidaEntity> bebidaEntity);

    @Named("mapTipoBebida")
    default TipoBebidaEntity mapTipoBebida(String descricaoTipoBebida) {
        Integer codigoTipoBebida = TipoBebida.getCodigoByDescricao(descricaoTipoBebida);
        return new TipoBebidaEntity(Long.valueOf(codigoTipoBebida), descricaoTipoBebida);
    }

    @Mapping(target = "bebidaId", source = "id.bebida.bebidaId")
    @Mapping(target = "nomeBebida", source = "id.bebida.nomeBebida")
    @Mapping(target = "tipoBebida", source = "id.bebida.tipoBebida", qualifiedByName = "mapDescricaoTipoBebida")
    @Mapping(target = "quantidadeBebida", source = "quantidadeBebida")
    @Mapping(target = "dataCadastro", source = "dataCadastro")
    BebidasNaSecaoResponseDto toBebidasNaSecaoResponseDto(BebidaSecaoEntity bebidaSecaoEntity);

    List<BebidasNaSecaoResponseDto> toBebidasNaSecaoResponseDtoList(List<BebidaSecaoEntity> bebidaSecaoEntities);

}