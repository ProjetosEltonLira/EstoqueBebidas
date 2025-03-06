package com.portifolio.bebidas.core.entities.mapper;

import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseBebidaHistoricoDTO;
import com.portifolio.bebidas.entrypoint.controller.dto.response.ResponseHistoricoDTO;
import com.portifolio.bebidas.core.entities.BebidaEntity;
import com.portifolio.bebidas.core.entities.HistoricoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
@Mapper(componentModel = "spring", uses = {BebidaMapper.class, SecaoMapper.class})
public interface HistoricoMapper {

    @Mapping(target = "id", source = "historicoId", qualifiedByName = "uuidToString")
    @Mapping(target = "bebida", source = ".", qualifiedByName = "mapBebidaWithVolume")
    @Mapping(target = "secao", source = "bebidaSecao.id.secao")
    ResponseHistoricoDTO toResponseDTO(HistoricoEntity entity);

    List<ResponseHistoricoDTO> toListResponseDTO(List<HistoricoEntity> entities);

    @Named("uuidToString")
    static String uuidToString(UUID uuid) {
        return (uuid != null) ? uuid.toString() : null;
    }

    @Named("mapBebidaWithVolume")
    default ResponseBebidaHistoricoDTO mapBebidaWithVolume(HistoricoEntity historicoEntity) {
        if (historicoEntity == null || historicoEntity.getBebidaSecao() == null || historicoEntity.getBebidaSecao().getId() == null) {
            return null;
        }

        BebidaEntity bebida = historicoEntity.getBebidaSecao().getId().getBebida();
        Double volumeBebida = historicoEntity.getVolumeBebida();

        return BebidaMapper.INSTANCE.toResponseBebidaHistoricoDTO(bebida, volumeBebida);
    }
}

