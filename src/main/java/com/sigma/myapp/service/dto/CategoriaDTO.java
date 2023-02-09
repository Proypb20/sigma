package com.sigma.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Categoria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double hourValue;

    private Double extra50;

    private Double extra100;

    private Double totalHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHourValue() {
        return hourValue;
    }

    public void setHourValue(Double hourValue) {
        this.hourValue = hourValue;
    }

    public Double getExtra50() {
        return extra50;
    }

    public void setExtra50(Double extra50) {
        this.extra50 = extra50;
    }

    public Double getExtra100() {
        return extra100;
    }

    public void setExtra100(Double extra100) {
        this.extra100 = extra100;
    }

    public Double getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(Double totalHour) {
        this.totalHour = totalHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaDTO)) {
            return false;
        }

        CategoriaDTO categoriaDTO = (CategoriaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", hourValue=" + getHourValue() +
            ", extra50=" + getExtra50() +
            ", extra100=" + getExtra100() +
            ", totalHour=" + getTotalHour() +
            "}";
    }
}
