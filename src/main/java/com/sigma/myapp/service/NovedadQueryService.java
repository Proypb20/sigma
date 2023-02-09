package com.sigma.myapp.service;

import com.sigma.myapp.domain.*; // for static metamodels
import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.repository.NovedadRepository;
import com.sigma.myapp.service.criteria.NovedadCriteria;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.service.mapper.NovedadMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Novedad} entities in the database.
 * The main input is a {@link NovedadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NovedadDTO} or a {@link Page} of {@link NovedadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NovedadQueryService extends QueryService<Novedad> {

    private final Logger log = LoggerFactory.getLogger(NovedadQueryService.class);

    private final NovedadRepository novedadRepository;

    private final NovedadMapper novedadMapper;

    public NovedadQueryService(NovedadRepository novedadRepository, NovedadMapper novedadMapper) {
        this.novedadRepository = novedadRepository;
        this.novedadMapper = novedadMapper;
    }

    /**
     * Return a {@link List} of {@link NovedadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NovedadDTO> findByCriteria(NovedadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Novedad> specification = createSpecification(criteria);
        return novedadMapper.toDto(novedadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NovedadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NovedadDTO> findByCriteria(NovedadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Novedad> specification = createSpecification(criteria);
        return novedadRepository.findAll(specification, page).map(novedadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NovedadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Novedad> specification = createSpecification(criteria);
        return novedadRepository.count(specification);
    }

    /**
     * Function to convert {@link NovedadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Novedad> createSpecification(NovedadCriteria criteria) {
        Specification<Novedad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Novedad_.id));
            }
            if (criteria.getTexto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTexto(), Novedad_.texto));
            }
            if (criteria.getEntregar() != null) {
                specification = specification.and(buildSpecification(criteria.getEntregar(), Novedad_.entregar));
            }
            if (criteria.getVigiladorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVigiladorId(),
                            root -> root.join(Novedad_.vigilador, JoinType.LEFT).get(Vigilador_.id)
                        )
                    );
            }
            if (criteria.getObjetivoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getObjetivoId(), root -> root.join(Novedad_.objetivo, JoinType.LEFT).get(Objetivo_.id))
                    );
            }
        }
        return specification;
    }
}
