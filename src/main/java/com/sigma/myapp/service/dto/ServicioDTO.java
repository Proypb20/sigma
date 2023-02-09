package com.sigma.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Servicio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startDate;

    private Instant endDate;

    private VigiladorDTO vigilador;

    private ObjetivoDTO objetivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public VigiladorDTO getVigilador() {
        return vigilador;
    }

    public void setVigilador(VigiladorDTO vigilador) {
        this.vigilador = vigilador;
    }

    public ObjetivoDTO getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoDTO objetivo) {
        this.objetivo = objetivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioDTO)) {
            return false;
        }

        ServicioDTO servicioDTO = (ServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", vigilador=" + getVigilador() +
            ", objetivo=" + getObjetivo() +
            "}";
    }
}
