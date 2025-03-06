package com.portifolio.bebidas.core.entities.mapper;


import com.portifolio.bebidas.core.entities.TipoBebidaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.context.annotation.Configuration;

@Configuration
@Mapper(componentModel = "spring")
public interface TipoBebidaMapper {

    @Named("mapDescricaoTipoBebida")
    default String mapDescricaoTipoBebida(TipoBebidaEntity tipoBebidaEntity) {
        return tipoBebidaEntity != null ? tipoBebidaEntity.getDescricao() : null;
    }
}