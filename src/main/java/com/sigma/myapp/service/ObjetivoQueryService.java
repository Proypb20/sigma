package com.sigma.myapp.service;

import com.sigma.myapp.domain.*; // for static metamodels
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.repository.ObjetivoRepository;
import com.sigma.myapp.service.criteria.ObjetivoCriteria;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import com.sigma.myapp.service.mapper.ObjetivoMapper;
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
 * Service for executing complex queries for {@link Objetivo} entities in the database.
 * The main input is a {@link ObjetivoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ObjetivoDTO} or a {@link Page} of {@link ObjetivoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ObjetivoQueryService extends QueryService<Objetivo> {

    private final Logger log = LoggerFactory.getLogger(ObjetivoQueryService.class);

    private final ObjetivoRepository objetivoRepository;

    private final ObjetivoMapper objetivoMapper;

    public ObjetivoQueryService(ObjetivoRepository objetivoRepository, ObjetivoMapper objetivoMapper) {
        this.objetivoRepository = objetivoRepository;
        this.objetivoMapper = objetivoMapper;
    }

    /**
     * Return a {@link List} of {@link ObjetivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ObjetivoDTO> findByCriteria(ObjetivoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Objetivo> specification = createSpecification(criteria);
        return objetivoMapper.toDto(objetivoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ObjetivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ObjetivoDTO> findByCriteria(ObjetivoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Objetivo> specification = createSpecification(criteria);
        return objetivoRepository.findAll(specification, page).map(objetivoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ObjetivoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Objetivo> specification = createSpecification(criteria);
        return objetivoRepository.count(specification);
    }

    /**
     * Function to convert {@link ObjetivoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Objetivo> createSpecification(ObjetivoCriteria criteria) {
        Specification<Objetivo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Objetivo_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Objetivo_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Objetivo_.address));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Objetivo_.city));
            }
        }
        return specification;
    }
}
