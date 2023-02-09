package com.sigma.myapp.service;

import com.sigma.myapp.domain.*; // for static metamodels
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.repository.VigiladorRepository;
import com.sigma.myapp.service.criteria.VigiladorCriteria;
import com.sigma.myapp.service.dto.VigiladorDTO;
import com.sigma.myapp.service.mapper.VigiladorMapper;
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
 * Service for executing complex queries for {@link Vigilador} entities in the database.
 * The main input is a {@link VigiladorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VigiladorDTO} or a {@link Page} of {@link VigiladorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VigiladorQueryService extends QueryService<Vigilador> {

    private final Logger log = LoggerFactory.getLogger(VigiladorQueryService.class);

    private final VigiladorRepository vigiladorRepository;

    private final VigiladorMapper vigiladorMapper;

    public VigiladorQueryService(VigiladorRepository vigiladorRepository, VigiladorMapper vigiladorMapper) {
        this.vigiladorRepository = vigiladorRepository;
        this.vigiladorMapper = vigiladorMapper;
    }

    /**
     * Return a {@link List} of {@link VigiladorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VigiladorDTO> findByCriteria(VigiladorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vigilador> specification = createSpecification(criteria);
        return vigiladorMapper.toDto(vigiladorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VigiladorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VigiladorDTO> findByCriteria(VigiladorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vigilador> specification = createSpecification(criteria);
        return vigiladorRepository.findAll(specification, page).map(vigiladorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VigiladorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vigilador> specification = createSpecification(criteria);
        return vigiladorRepository.count(specification);
    }

    /**
     * Function to convert {@link VigiladorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vigilador> createSpecification(VigiladorCriteria criteria) {
        Specification<Vigilador> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vigilador_.id));
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Vigilador_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
            if (criteria.getObjetivoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getObjetivoId(),
                            root -> root.join(Vigilador_.objetivo, JoinType.LEFT).get(Objetivo_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Vigilador_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
