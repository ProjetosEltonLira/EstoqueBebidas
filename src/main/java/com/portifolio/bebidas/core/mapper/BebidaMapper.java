package com.portifolio.bebidas.core.mapper;


import com.portifolio.bebidas.entrypoint.controller.dto.response.BebidasNaSecaoResponseDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseBebidaHistoricoDTO;
import com.portifolio.bebidas.core.enums.TipoBebida;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseFindAllBebidasDto;
import com.portifolio.bebidas.core.entities.BebidaEntity;
import com.portifolio.bebidas.core.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.core.entities.TipoBebidaEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Mapper(componentModel = "spring",uses = {TipoBebidaMapper.class})
public interface BebidaMapper {

    BebidaMapper INSTANCE = Mappers.getMapper(BebidaMapper.class);

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
    BebidasNaSecaoResponseDTO toBebidasNaSecaoResponseDto(BebidaSecaoEntity bebidaSecaoEntity);

    List<BebidasNaSecaoResponseDTO> toBebidasNaSecaoResponseDtoList(List<BebidaSecaoEntity> bebidaSecaoEntities);

    default ResponseBebidaHistoricoDTO toResponseBebidaHistoricoDTO(BebidaEntity bebidaEntity, Double volumeBebida) {
        if (bebidaEntity == null) {
            return null;
        }
        return new ResponseBebidaHistoricoDTO(
                bebidaEntity.getBebidaId(),
                bebidaEntity.getNomeBebida(),
                volumeBebida
        );
    }
}