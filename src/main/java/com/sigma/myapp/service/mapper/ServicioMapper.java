package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Servicio;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.service.dto.ServicioDTO;
import com.sigma.myapp.service.dto.VigiladorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
    @Mapping(target = "vigilador", source = "vigilador", qualifiedByName = "vigiladorId")
    ServicioDTO toDto(Servicio s);

    @Named("vigiladorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "objetivo", source = "objetivo")
    VigiladorDTO toDtoVigiladorId(Vigilador vigilador);
}
