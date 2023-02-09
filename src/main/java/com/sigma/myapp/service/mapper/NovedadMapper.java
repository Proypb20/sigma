package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import com.sigma.myapp.service.dto.VigiladorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Novedad} and its DTO {@link NovedadDTO}.
 */
@Mapper(componentModel = "spring")
public interface NovedadMapper extends EntityMapper<NovedadDTO, Novedad> {
    @Mapping(target = "vigilador", source = "vigilador", qualifiedByName = "vigiladorId")
    @Mapping(target = "objetivo", source = "objetivo", qualifiedByName = "objetivoId")
    NovedadDTO toDto(Novedad s);

    @Named("vigiladorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VigiladorDTO toDtoVigiladorId(Vigilador vigilador);

    @Named("objetivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObjetivoDTO toDtoObjetivoId(Objetivo objetivo);
}
