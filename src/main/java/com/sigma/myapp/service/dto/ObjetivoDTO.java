package com.sigma.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Objetivo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ObjetivoDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String address;

    private String city;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjetivoDTO)) {
            return false;
        }

        ObjetivoDTO objetivoDTO = (ObjetivoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, objetivoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObjetivoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
