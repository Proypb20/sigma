package com.sigma.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sigma.myapp.domain.Vigilador} entity. This class is used
 * in {@link com.sigma.myapp.web.rest.VigiladorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vigiladors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VigiladorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter categoriaId;

    private LongFilter objetivoId;

    private LongFilter userId;

    private Boolean distinct;

    public VigiladorCriteria() {}

    public VigiladorCriteria(VigiladorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.objetivoId = other.objetivoId == null ? null : other.objetivoId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VigiladorCriteria copy() {
        return new VigiladorCriteria(this);
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

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LongFilter getObjetivoId() {
        return objetivoId;
    }

    public LongFilter objetivoId() {
        if (objetivoId == null) {
            objetivoId = new LongFilter();
        }
        return objetivoId;
    }

    public void setObjetivoId(LongFilter objetivoId) {
        this.objetivoId = objetivoId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final VigiladorCriteria that = (VigiladorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(objetivoId, that.objetivoId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoriaId, objetivoId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VigiladorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (objetivoId != null ? "objetivoId=" + objetivoId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
