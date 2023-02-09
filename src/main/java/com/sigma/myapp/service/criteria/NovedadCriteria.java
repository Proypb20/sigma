package com.sigma.myapp.service.criteria;

import com.sigma.myapp.domain.enumeration.Entregar;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sigma.myapp.domain.Novedad} entity. This class is used
 * in {@link com.sigma.myapp.web.rest.NovedadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /novedads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NovedadCriteria implements Serializable, Criteria {

    /**
     * Class for filtering entregar
     */
    public static class entregarFilter extends Filter<Entregar> {

        public entregarFilter() {}

        public entregarFilter(entregarFilter filter) {
            super(filter);
        }

        @Override
        public entregarFilter copy() {
            return new entregarFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter texto;

    private entregarFilter entregar;

    private LongFilter vigiladorId;

    private LongFilter objetivoId;

    private Boolean distinct;

    public NovedadCriteria() {}

    public NovedadCriteria(NovedadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.texto = other.texto == null ? null : other.texto.copy();
        this.entregar = other.entregar == null ? null : other.entregar.copy();
        this.vigiladorId = other.vigiladorId == null ? null : other.vigiladorId.copy();
        this.objetivoId = other.objetivoId == null ? null : other.objetivoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NovedadCriteria copy() {
        return new NovedadCriteria(this);
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

    public StringFilter getTexto() {
        return texto;
    }

    public StringFilter texto() {
        if (texto == null) {
            texto = new StringFilter();
        }
        return texto;
    }

    public void setTexto(StringFilter texto) {
        this.texto = texto;
    }

    public entregarFilter getEntregar() {
        return entregar;
    }

    public entregarFilter entregar() {
        if (entregar == null) {
            entregar = new entregarFilter();
        }
        return entregar;
    }

    public void setEntregar(entregarFilter entregar) {
        this.entregar = entregar;
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
        final NovedadCriteria that = (NovedadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(texto, that.texto) &&
            Objects.equals(entregar, that.entregar) &&
            Objects.equals(vigiladorId, that.vigiladorId) &&
            Objects.equals(objetivoId, that.objetivoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto, entregar, vigiladorId, objetivoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NovedadCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (texto != null ? "texto=" + texto + ", " : "") +
            (entregar != null ? "entregar=" + entregar + ", " : "") +
            (vigiladorId != null ? "vigiladorId=" + vigiladorId + ", " : "") +
            (objetivoId != null ? "objetivoId=" + objetivoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
