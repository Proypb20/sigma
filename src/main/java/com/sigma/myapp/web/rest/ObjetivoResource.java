package com.sigma.myapp.web.rest;

import com.sigma.myapp.repository.ObjetivoRepository;
import com.sigma.myapp.service.ObjetivoQueryService;
import com.sigma.myapp.service.ObjetivoService;
import com.sigma.myapp.service.criteria.ObjetivoCriteria;
import com.sigma.myapp.service.dto.ObjetivoDTO;
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
 * REST controller for managing {@link com.sigma.myapp.domain.Objetivo}.
 */
@RestController
@RequestMapping("/api")
public class ObjetivoResource {

    private final Logger log = LoggerFactory.getLogger(ObjetivoResource.class);

    private static final String ENTITY_NAME = "objetivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjetivoService objetivoService;

    private final ObjetivoRepository objetivoRepository;

    private final ObjetivoQueryService objetivoQueryService;

    public ObjetivoResource(
        ObjetivoService objetivoService,
        ObjetivoRepository objetivoRepository,
        ObjetivoQueryService objetivoQueryService
    ) {
        this.objetivoService = objetivoService;
        this.objetivoRepository = objetivoRepository;
        this.objetivoQueryService = objetivoQueryService;
    }

    /**
     * {@code POST  /objetivos} : Create a new objetivo.
     *
     * @param objetivoDTO the objetivoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objetivoDTO, or with status {@code 400 (Bad Request)} if the objetivo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/objetivos")
    public ResponseEntity<ObjetivoDTO> createObjetivo(@Valid @RequestBody ObjetivoDTO objetivoDTO) throws URISyntaxException {
        log.debug("REST request to save Objetivo : {}", objetivoDTO);
        if (objetivoDTO.getId() != null) {
            throw new BadRequestAlertException("A new objetivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ObjetivoDTO result = objetivoService.save(objetivoDTO);
        return ResponseEntity
            .created(new URI("/api/objetivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /objetivos/:id} : Updates an existing objetivo.
     *
     * @param id the id of the objetivoDTO to save.
     * @param objetivoDTO the objetivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objetivoDTO,
     * or with status {@code 400 (Bad Request)} if the objetivoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objetivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objetivos/{id}")
    public ResponseEntity<ObjetivoDTO> updateObjetivo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ObjetivoDTO objetivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Objetivo : {}, {}", id, objetivoDTO);
        if (objetivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objetivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objetivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ObjetivoDTO result = objetivoService.update(objetivoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objetivoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /objetivos/:id} : Partial updates given fields of an existing objetivo, field will ignore if it is null
     *
     * @param id the id of the objetivoDTO to save.
     * @param objetivoDTO the objetivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objetivoDTO,
     * or with status {@code 400 (Bad Request)} if the objetivoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the objetivoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the objetivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objetivos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ObjetivoDTO> partialUpdateObjetivo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ObjetivoDTO objetivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objetivo partially : {}, {}", id, objetivoDTO);
        if (objetivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objetivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objetivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ObjetivoDTO> result = objetivoService.partialUpdate(objetivoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objetivoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /objetivos} : get all the objetivos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objetivos in body.
     */
    @GetMapping("/objetivos")
    public ResponseEntity<List<ObjetivoDTO>> getAllObjetivos(ObjetivoCriteria criteria) {
        log.debug("REST request to get Objetivos by criteria: {}", criteria);
        List<ObjetivoDTO> entityList = objetivoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /objetivos/count} : count all the objetivos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/objetivos/count")
    public ResponseEntity<Long> countObjetivos(ObjetivoCriteria criteria) {
        log.debug("REST request to count Objetivos by criteria: {}", criteria);
        return ResponseEntity.ok().body(objetivoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /objetivos/:id} : get the "id" objetivo.
     *
     * @param id the id of the objetivoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objetivoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objetivos/{id}")
    public ResponseEntity<ObjetivoDTO> getObjetivo(@PathVariable Long id) {
        log.debug("REST request to get Objetivo : {}", id);
        Optional<ObjetivoDTO> objetivoDTO = objetivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(objetivoDTO);
    }

    /**
     * {@code DELETE  /objetivos/:id} : delete the "id" objetivo.
     *
     * @param id the id of the objetivoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objetivos/{id}")
    public ResponseEntity<Void> deleteObjetivo(@PathVariable Long id) {
        log.debug("REST request to delete Objetivo : {}", id);
        objetivoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
