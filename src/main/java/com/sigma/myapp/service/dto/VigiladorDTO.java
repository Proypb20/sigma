package com.sigma.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Vigilador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VigiladorDTO implements Serializable {

    private Long id;

    private CategoriaDTO categoria;

    private ObjetivoDTO objetivo;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    public ObjetivoDTO getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoDTO objetivo) {
        this.objetivo = objetivo;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VigiladorDTO)) {
            return false;
        }

        VigiladorDTO vigiladorDTO = (VigiladorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vigiladorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VigiladorDTO{" +
            "id=" + getId() +
            ", categoria=" + getCategoria() +
            ", objetivo=" + getObjetivo() +
            ", user=" + getUser() +
            "}";
    }
}
