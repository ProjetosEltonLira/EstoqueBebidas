package com.portifolio.bebidas.entities.mapper;

import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;
import com.portifolio.bebidas.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.entities.SecaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Mapper(componentModel = "spring", uses = {BebidaMapper.class})
public interface SecaoMapper {

    SecaoMapper INSTANCE = Mappers.getMapper(SecaoMapper.class);

    @Mapping(target = "secaoId", source = "secaoId")
    @Mapping(target = "nomeSecao", source = "nomeSecao")
    @Mapping(target = "tipoSecao", source = "tipoBebida.descricao") // Mapear a descrição do tipo de bebida
    @Mapping(target = "quantidadeTotal", expression = "java(calcularQuantidadeTotal(entity.getBebidaSecaoEntities()))") // Calcular quantidade total
    @Mapping(target = "bebidas", source = "bebidaSecaoEntities") // Lista de bebidas
    ResponseSecaoDto toResponseSecaoDto(SecaoEntity entity);

    default Double calcularQuantidadeTotal(List<BebidaSecaoEntity> bebidaSecaoEntities) {
        if (bebidaSecaoEntities == null || bebidaSecaoEntities.isEmpty()) {
            return 0.0;
        }
        return bebidaSecaoEntities.stream()
                .mapToDouble(BebidaSecaoEntity::getQuantidadeBebida)
                .sum();
    }
//    @Named("mapQuantidadeTotalBebidasNaSecao")
//    default double mapQuantidadeTotalBebidasNaSecao(List<BebidaSecaoEntity> bebidaSecao) {
//        return bebidaSecao.stream()
//                .map(BebidaSecaoEntity::getQuantidadeBebida)
//                .filter(q -> q != null && q > 0)
//                .mapToDouble(Double::doubleValue)
//                .sum();
//    }
}
