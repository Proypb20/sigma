package com.sigma.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sigma.myapp.domain.Servicio} entity. This class is used
 * in {@link com.sigma.myapp.web.rest.ServicioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /servicios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private LongFilter vigiladorId;

    private Boolean distinct;

    public ServicioCriteria() {}

    public ServicioCriteria(ServicioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.vigiladorId = other.vigiladorId == null ? null : other.vigiladorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ServicioCriteria copy() {
        return new ServicioCriteria(this);
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

    public InstantFilter getStartDate() {
        return startDate;
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            startDate = new InstantFilter();
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            endDate = new InstantFilter();
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getVigiladorId() {
        return vigiladorId;
    }

    public LongFilter vigiladorId() {
        if (vigiladorId == null) {
            vigiladorId = new LongFilter();
        }
        return vigiladorId;
    }

    public void setVigiladorId(LongFilter vigiladorId) {
        this.vigiladorId = vigiladorId;
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
        final ServicioCriteria that = (ServicioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(vigiladorId, that.vigiladorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, vigiladorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (vigiladorId != null ? "vigiladorId=" + vigiladorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
