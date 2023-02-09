package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Notificacion;
import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.service.dto.NotificacionDTO;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.service.dto.VigiladorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacion} and its DTO {@link NotificacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificacionMapper extends EntityMapper<NotificacionDTO, Notificacion> {
    @Mapping(target = "novedad", source = "novedad", qualifiedByName = "novedadId")
    @Mapping(target = "vigilador", source = "vigilador", qualifiedByName = "vigiladorId")
    NotificacionDTO toDto(Notificacion s);

    @Named("novedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "texto", source = "texto")
    @Mapping(target = "picture", source = "picture")
    @Mapping(target = "pictureContentType", source = "pictureContentType")
    NovedadDTO toDtoNovedadId(Novedad novedad);

    @Named("vigiladorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    VigiladorDTO toDtoVigiladorId(Vigilador vigilador);
}
