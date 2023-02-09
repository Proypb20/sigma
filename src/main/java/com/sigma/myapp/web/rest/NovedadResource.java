package com.sigma.myapp.web.rest;

import com.sigma.myapp.repository.NovedadRepository;
import com.sigma.myapp.service.NovedadQueryService;
import com.sigma.myapp.service.NovedadService;
import com.sigma.myapp.service.criteria.NovedadCriteria;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sigma.myapp.domain.Novedad}.
 */
@RestController
@RequestMapping("/api")
public class NovedadResource {

    private final Logger log = LoggerFactory.getLogger(NovedadResource.class);

    private static final String ENTITY_NAME = "novedad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NovedadService novedadService;

    private final NovedadRepository novedadRepository;

    private final NovedadQueryService novedadQueryService;

    public NovedadResource(NovedadService novedadService, NovedadRepository novedadRepository, NovedadQueryService novedadQueryService) {
        this.novedadService = novedadService;
        this.novedadRepository = novedadRepository;
        this.novedadQueryService = novedadQueryService;
    }

    /**
     * {@code POST  /novedads} : Create a new novedad.
     *
     * @param novedadDTO the novedadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new novedadDTO, or with status {@code 400 (Bad Request)} if the novedad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/novedads")
    public ResponseEntity<NovedadDTO> createNovedad(@Valid @RequestBody NovedadDTO novedadDTO) throws URISyntaxException {
        log.debug("REST request to save Novedad : {}", novedadDTO);
        if (novedadDTO.getId() != null) {
            throw new BadRequestAlertException("A new novedad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NovedadDTO result = novedadService.save(novedadDTO);
        return ResponseEntity
            .created(new URI("/api/novedads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /novedads/:id} : Updates an existing novedad.
     *
     * @param id the id of the novedadDTO to save.
     * @param novedadDTO the novedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated novedadDTO,
     * or with status {@code 400 (Bad Request)} if the novedadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the novedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/novedads/{id}")
    public ResponseEntity<NovedadDTO> updateNovedad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NovedadDTO novedadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Novedad : {}, {}", id, novedadDTO);
        if (novedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, novedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!novedadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NovedadDTO result = novedadService.update(novedadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, novedadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /novedads/:id} : Partial updates given fields of an existing novedad, field will ignore if it is null
     *
     * @param id the id of the novedadDTO to save.
     * @param novedadDTO the novedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated novedadDTO,
     * or with status {@code 400 (Bad Request)} if the novedadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the novedadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the novedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/novedads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NovedadDTO> partialUpdateNovedad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NovedadDTO novedadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Novedad partially : {}, {}", id, novedadDTO);
        if (novedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, novedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!novedadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NovedadDTO> result = novedadService.partialUpdate(novedadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, novedadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /novedads} : get all the novedads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of novedads in body.
     */
    @GetMapping("/novedads")
    public ResponseEntity<List<NovedadDTO>> getAllNovedads(NovedadCriteria criteria) {
        log.debug("REST request to get Novedads by criteria: {}", criteria);
        List<NovedadDTO> entityList = novedadQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /novedads/count} : count all the novedads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/novedads/count")
    public ResponseEntity<Long> countNovedads(NovedadCriteria criteria) {
        log.debug("REST request to count Novedads by criteria: {}", criteria);
        return ResponseEntity.ok().body(novedadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /novedads/:id} : get the "id" novedad.
     *
     * @param id the id of the novedadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the novedadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/novedads/{id}")
    public ResponseEntity<NovedadDTO> getNovedad(@PathVariable Long id) {
        log.debug("REST request to get Novedad : {}", id);
        Optional<NovedadDTO> novedadDTO = novedadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(novedadDTO);
    }

    /**
     * {@code DELETE  /novedads/:id} : delete the "id" novedad.
     *
     * @param id the id of the novedadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/novedads/{id}")
    public ResponseEntity<Void> deleteNovedad(@PathVariable Long id) {
        log.debug("REST request to delete Novedad : {}", id);
        novedadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
