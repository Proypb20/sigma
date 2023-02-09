package com.sigma.myapp.service.dto;

import com.sigma.myapp.domain.enumeration.Entregar;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.sigma.myapp.domain.Novedad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NovedadDTO implements Serializable {

    private Long id;

    @NotNull
    private String texto;

    @Lob
    private byte[] picture;

    private String pictureContentType;

    @NotNull
    private Entregar entregar;

    private VigiladorDTO vigilador;

    private ObjetivoDTO objetivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Entregar getEntregar() {
        return entregar;
    }

    public void setEntregar(Entregar entregar) {
        this.entregar = entregar;
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
        if (!(o instanceof NovedadDTO)) {
            return false;
        }

        NovedadDTO novedadDTO = (NovedadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, novedadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NovedadDTO{" +
            "id=" + getId() +
            ", texto='" + getTexto() + "'" +
            ", picture='" + getPicture() + "'" +
            ", entregar='" + getEntregar() + "'" +
            ", vigilador=" + getVigilador() +
            ", objetivo=" + getObjetivo() +
            "}";
    }
}
