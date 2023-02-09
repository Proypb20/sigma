package com.sigma.myapp.service.mapper;

import com.sigma.myapp.domain.Categoria;
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.domain.User;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.service.dto.CategoriaDTO;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import com.sigma.myapp.service.dto.UserDTO;
import com.sigma.myapp.service.dto.VigiladorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vigilador} and its DTO {@link VigiladorDTO}.
 */
@Mapper(componentModel = "spring")
public interface VigiladorMapper extends EntityMapper<VigiladorDTO, Vigilador> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaName")
    @Mapping(target = "objetivo", source = "objetivo", qualifiedByName = "objetivoName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    VigiladorDTO toDto(Vigilador s);

    @Named("categoriaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoriaDTO toDtoCategoriaName(Categoria categoria);

    @Named("objetivoName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ObjetivoDTO toDtoObjetivoName(Objetivo objetivo);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    UserDTO toDtoUserLogin(User user);
}
