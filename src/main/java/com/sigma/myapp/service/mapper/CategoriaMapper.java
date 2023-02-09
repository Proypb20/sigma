package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Categoria;
import com.sigma.myapp.service.dto.CategoriaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {}
