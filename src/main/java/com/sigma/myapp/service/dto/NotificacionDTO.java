package com.sigma.myapp.service.dto;

import com.sigma.myapp.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Notificacion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacionDTO implements Serializable {

    private Long id;

    @NotNull
    private Status status;

    private NovedadDTO novedad;

    private VigiladorDTO vigilador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public NovedadDTO getNovedad() {
        return novedad;
    }

    public void setNovedad(NovedadDTO novedad) {
        this.novedad = novedad;
    }

    public VigiladorDTO getVigilador() {
        return vigilador;
    }

    public void setVigilador(VigiladorDTO vigilador) {
        this.vigilador = vigilador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificacionDTO)) {
            return false;
        }

        NotificacionDTO notificacionDTO = (NotificacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacionDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", novedad=" + getNovedad() +
            ", vigilador=" + getVigilador() +
            "}";
    }
}
