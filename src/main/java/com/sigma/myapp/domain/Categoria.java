package com.sigma.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Categoria.
 */
@Entity
@Table(name = "categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hour_value")
    private Double hourValue;

    @Column(name = "extra_50")
    private Double extra50;

    @Column(name = "extra_100")
    private Double extra100;

    @Column(name = "total_hour")
    private Double totalHour;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categoria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Categoria name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHourValue() {
        return this.hourValue;
    }

    public Categoria hourValue(Double hourValue) {
        this.setHourValue(hourValue);
        return this;
    }

    public void setHourValue(Double hourValue) {
        this.hourValue = hourValue;
    }

    public Double getExtra50() {
        return this.extra50;
    }

    public Categoria extra50(Double extra50) {
        this.setExtra50(extra50);
        return this;
    }

    public void setExtra50(Double extra50) {
        this.extra50 = extra50;
    }

    public Double getExtra100() {
        return this.extra100;
    }

    public Categoria extra100(Double extra100) {
        this.setExtra100(extra100);
        return this;
    }

    public void setExtra100(Double extra100) {
        this.extra100 = extra100;
    }

    public Double getTotalHour() {
        return this.totalHour;
    }

    public Categoria totalHour(Double totalHour) {
        this.setTotalHour(totalHour);
        return this;
    }

    public void setTotalHour(Double totalHour) {
        this.totalHour = totalHour;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return id != null && id.equals(((Categoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", hourValue=" + getHourValue() +
            ", extra50=" + getExtra50() +
            ", extra100=" + getExtra100() +
            ", totalHour=" + getTotalHour() +
            "}";
    }
}
