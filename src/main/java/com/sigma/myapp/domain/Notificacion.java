package com.sigma.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sigma.myapp.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacion.
 */
@Entity
@Table(name = "notificacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    private Novedad novedad;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categoria", "objetivo", "user" }, allowSetters = true)
    private Vigilador vigilador;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notificacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public Notificacion status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Novedad getNovedad() {
        return this.novedad;
    }

    public void setNovedad(Novedad novedad) {
        this.novedad = novedad;
    }

    public Notificacion novedad(Novedad novedad) {
        this.setNovedad(novedad);
        return this;
    }

    public Vigilador getVigilador() {
        return this.vigilador;
    }

    public void setVigilador(Vigilador vigilador) {
        this.vigilador = vigilador;
    }

    public Notificacion vigilador(Vigilador vigilador) {
        this.setVigilador(vigilador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }
        return id != null && id.equals(((Notificacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacion{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
