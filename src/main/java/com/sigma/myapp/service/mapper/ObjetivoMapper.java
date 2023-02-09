package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Objetivo} and its DTO {@link ObjetivoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ObjetivoMapper extends EntityMapper<ObjetivoDTO, Objetivo> {}
