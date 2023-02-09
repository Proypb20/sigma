package com.sigma.myapp.service.criteria;

import com.sigma.myapp.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sigma.myapp.domain.Notificacion} entity. This class is used
 * in {@link com.sigma.myapp.web.rest.NotificacionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notificacions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusFilter status;

    private LongFilter novedadId;

    private LongFilter vigiladorId;

    private Boolean distinct;

    public NotificacionCriteria() {}

    public NotificacionCriteria(NotificacionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.novedadId = other.novedadId == null ? null : other.novedadId.copy();
        this.vigiladorId = other.vigiladorId == null ? null : other.vigiladorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificacionCriteria copy() {
        return new NotificacionCriteria(this);
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

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getNovedadId() {
        return novedadId;
    }

    public LongFilter novedadId() {
        if (novedadId == null) {
            novedadId = new LongFilter();
        }
        return novedadId;
    }

    public void setNovedadId(LongFilter novedadId) {
        this.novedadId = novedadId;
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
        final NotificacionCriteria that = (NotificacionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(novedadId, that.novedadId) &&
            Objects.equals(vigiladorId, that.vigiladorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, novedadId, vigiladorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (novedadId != null ? "novedadId=" + novedadId + ", " : "") +
            (vigiladorId != null ? "vigiladorId=" + vigiladorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
