package com.portifolio.bebidas.entities.mapper;

import com.portifolio.bebidas.controller.dto.response.ResponseBebidaHistoricoDTO;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoDto;
import com.portifolio.bebidas.controller.dto.response.ResponseSecaoHistoricoDTO;
import com.portifolio.bebidas.entities.BebidaEntity;
import com.portifolio.bebidas.entities.BebidaSecaoEntity;
import com.portifolio.bebidas.entities.SecaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Mapper(componentModel = "spring", uses = {BebidaMapper.class})
public interface SecaoMapper {

    SecaoMapper INSTANCE = Mappers.getMapper(SecaoMapper.class);

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

    @Mapping(target = "id", source = "secaoId")
    @Mapping(target = "nome", source = "nomeSecao")
    ResponseSecaoHistoricoDTO toResponseSecaoHistoricoDTO(SecaoEntity secaoEntity);

}
