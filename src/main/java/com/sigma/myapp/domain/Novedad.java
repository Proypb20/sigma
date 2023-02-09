package com.sigma.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sigma.myapp.domain.enumeration.Entregar;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Novedad.
 */
@Entity
@Table(name = "novedad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Novedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "texto", nullable = false)
    private String texto;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entregar", nullable = false)
    private Entregar entregar;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categoria", "objetivo", "user" }, allowSetters = true)
    private Vigilador vigilador;

    @ManyToOne
    private Objetivo objetivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Novedad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return this.texto;
    }

    public Novedad texto(String texto) {
        this.setTexto(texto);
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Novedad picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Novedad pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Entregar getEntregar() {
        return this.entregar;
    }

    public Novedad entregar(Entregar entregar) {
        this.setEntregar(entregar);
        return this;
    }

    public void setEntregar(Entregar entregar) {
        this.entregar = entregar;
    }

    public Vigilador getVigilador() {
        return this.vigilador;
    }

    public void setVigilador(Vigilador vigilador) {
        this.vigilador = vigilador;
    }

    public Novedad vigilador(Vigilador vigilador) {
        this.setVigilador(vigilador);
        return this;
    }

    public Objetivo getObjetivo() {
        return this.objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public Novedad objetivo(Objetivo objetivo) {
        this.setObjetivo(objetivo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Novedad)) {
            return false;
        }
        return id != null && id.equals(((Novedad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Novedad{" +
            "id=" + getId() +
            ", texto='" + getTexto() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", entregar='" + getEntregar() + "'" +
            "}";
    }
}
