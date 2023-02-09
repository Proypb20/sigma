package com.sigma.myapp.web.rest;

import com.sigma.myapp.repository.VigiladorRepository;
import com.sigma.myapp.service.VigiladorQueryService;
import com.sigma.myapp.service.VigiladorService;
import com.sigma.myapp.service.criteria.VigiladorCriteria;
import com.sigma.myapp.service.dto.VigiladorDTO;
import com.sigma.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sigma.myapp.domain.Vigilador}.
 */
@RestController
@RequestMapping("/api")
public class VigiladorResource {

    private final Logger log = LoggerFactory.getLogger(VigiladorResource.class);

    private static final String ENTITY_NAME = "vigilador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VigiladorService vigiladorService;

    private final VigiladorRepository vigiladorRepository;

    private final VigiladorQueryService vigiladorQueryService;

    public VigiladorResource(
        VigiladorService vigiladorService,
        VigiladorRepository vigiladorRepository,
        VigiladorQueryService vigiladorQueryService
    ) {
        this.vigiladorService = vigiladorService;
        this.vigiladorRepository = vigiladorRepository;
        this.vigiladorQueryService = vigiladorQueryService;
    }

    /**
     * {@code POST  /vigiladors} : Create a new vigilador.
     *
     * @param vigiladorDTO the vigiladorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vigiladorDTO, or with status {@code 400 (Bad Request)} if the vigilador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vigiladors")
    public ResponseEntity<VigiladorDTO> createVigilador(@RequestBody VigiladorDTO vigiladorDTO) throws URISyntaxException {
        log.debug("REST request to save Vigilador : {}", vigiladorDTO);
        if (vigiladorDTO.getId() != null) {
            throw new BadRequestAlertException("A new vigilador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(vigiladorDTO.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        VigiladorDTO result = vigiladorService.save(vigiladorDTO);
        return ResponseEntity
            .created(new URI("/api/vigiladors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vigiladors/:id} : Updates an existing vigilador.
     *
     * @param id the id of the vigiladorDTO to save.
     * @param vigiladorDTO the vigiladorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vigiladorDTO,
     * or with status {@code 400 (Bad Request)} if the vigiladorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vigiladorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vigiladors/{id}")
    public ResponseEntity<VigiladorDTO> updateVigilador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VigiladorDTO vigiladorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vigilador : {}, {}", id, vigiladorDTO);
        if (vigiladorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vigiladorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vigiladorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VigiladorDTO result = vigiladorService.update(vigiladorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vigiladorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vigiladors/:id} : Partial updates given fields of an existing vigilador, field will ignore if it is null
     *
     * @param id the id of the vigiladorDTO to save.
     * @param vigiladorDTO the vigiladorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vigiladorDTO,
     * or with status {@code 400 (Bad Request)} if the vigiladorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vigiladorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vigiladorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vigiladors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VigiladorDTO> partialUpdateVigilador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VigiladorDTO vigiladorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vigilador partially : {}, {}", id, vigiladorDTO);
        if (vigiladorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vigiladorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vigiladorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VigiladorDTO> result = vigiladorService.partialUpdate(vigiladorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vigiladorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vigiladors} : get all the vigiladors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vigiladors in body.
     */
    @GetMapping("/vigiladors")
    public ResponseEntity<List<VigiladorDTO>> getAllVigiladors(VigiladorCriteria criteria) {
        log.debug("REST request to get Vigiladors by criteria: {}", criteria);
        List<VigiladorDTO> entityList = vigiladorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /vigiladors/count} : count all the vigiladors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vigiladors/count")
    public ResponseEntity<Long> countVigiladors(VigiladorCriteria criteria) {
        log.debug("REST request to count Vigiladors by criteria: {}", criteria);
        return ResponseEntity.ok().body(vigiladorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vigiladors/:id} : get the "id" vigilador.
     *
     * @param id the id of the vigiladorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vigiladorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vigiladors/{id}")
    public ResponseEntity<VigiladorDTO> getVigilador(@PathVariable Long id) {
        log.debug("REST request to get Vigilador : {}", id);
        Optional<VigiladorDTO> vigiladorDTO = vigiladorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vigiladorDTO);
    }

    /**
     * {@code DELETE  /vigiladors/:id} : delete the "id" vigilador.
     *
     * @param id the id of the vigiladorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vigiladors/{id}")
    public ResponseEntity<Void> deleteVigilador(@PathVariable Long id) {
        log.debug("REST request to delete Vigilador : {}", id);
        vigiladorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
