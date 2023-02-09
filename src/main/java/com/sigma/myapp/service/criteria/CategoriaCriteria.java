package com.sigma.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sigma.myapp.domain.Categoria} entity. This class is used
 * in {@link com.sigma.myapp.web.rest.CategoriaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categorias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter hourValue;

    private DoubleFilter extra50;

    private DoubleFilter extra100;

    private DoubleFilter totalHour;

    private Boolean distinct;

    public CategoriaCriteria() {}

    public CategoriaCriteria(CategoriaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.hourValue = other.hourValue == null ? null : other.hourValue.copy();
        this.extra50 = other.extra50 == null ? null : other.extra50.copy();
        this.extra100 = other.extra100 == null ? null : other.extra100.copy();
        this.totalHour = other.totalHour == null ? null : other.totalHour.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CategoriaCriteria copy() {
        return new CategoriaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getHourValue() {
        return hourValue;
    }

    public DoubleFilter hourValue() {
        if (hourValue == null) {
            hourValue = new DoubleFilter();
        }
        return hourValue;
    }

    public void setHourValue(DoubleFilter hourValue) {
        this.hourValue = hourValue;
    }

    public DoubleFilter getExtra50() {
        return extra50;
    }

    public DoubleFilter extra50() {
        if (extra50 == null) {
            extra50 = new DoubleFilter();
        }
        return extra50;
    }

    public void setExtra50(DoubleFilter extra50) {
        this.extra50 = extra50;
    }

    public DoubleFilter getExtra100() {
        return extra100;
    }

    public DoubleFilter extra100() {
        if (extra100 == null) {
            extra100 = new DoubleFilter();
        }
        return extra100;
    }

    public void setExtra100(DoubleFilter extra100) {
        this.extra100 = extra100;
    }

    public DoubleFilter getTotalHour() {
        return totalHour;
    }

    public DoubleFilter totalHour() {
        if (totalHour == null) {
            totalHour = new DoubleFilter();
        }
        return totalHour;
    }

    public void setTotalHour(DoubleFilter totalHour) {
        this.totalHour = totalHour;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoriaCriteria that = (CategoriaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(hourValue, that.hourValue) &&
            Objects.equals(extra50, that.extra50) &&
            Objects.equals(extra100, that.extra100) &&
            Objects.equals(totalHour, that.totalHour) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hourValue, extra50, extra100, totalHour, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (hourValue != null ? "hourValue=" + hourValue + ", " : "") +
            (extra50 != null ? "extra50=" + extra50 + ", " : "") +
            (extra100 != null ? "extra100=" + extra100 + ", " : "") +
            (totalHour != null ? "totalHour=" + totalHour + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
