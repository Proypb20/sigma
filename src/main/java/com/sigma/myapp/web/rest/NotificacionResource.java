package com.sigma.myapp.web.rest;

import com.sigma.myapp.domain.Notificacion;
import com.sigma.myapp.domain.enumeration.Status;
import com.sigma.myapp.repository.NotificacionRepository;
import com.sigma.myapp.service.NotificacionQueryService;
import com.sigma.myapp.service.NotificacionService;
import com.sigma.myapp.service.criteria.NotificacionCriteria;
import com.sigma.myapp.service.dto.NotificacionDTO;
import com.sigma.myapp.service.mapper.NotificacionMapper;
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
 * REST controller for managing {@link com.sigma.myapp.domain.Notificacion}.
 */
@RestController
@RequestMapping("/api")
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private static final String ENTITY_NAME = "notificacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacionService notificacionService;

    private final NotificacionRepository notificacionRepository;

    private final NotificacionQueryService notificacionQueryService;

    private final NotificacionMapper notificacionMapper;

    public NotificacionResource(
        NotificacionService notificacionService,
        NotificacionRepository notificacionRepository,
        NotificacionQueryService notificacionQueryService,
        NotificacionMapper notificacionMapper
    ) {
        this.notificacionService = notificacionService;
        this.notificacionRepository = notificacionRepository;
        this.notificacionQueryService = notificacionQueryService;
        this.notificacionMapper = notificacionMapper;
    }

    /**
     * {@code POST  /notificacions} : Create a new notificacion.
     *
     * @param notificacionDTO the notificacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacionDTO, or with status {@code 400 (Bad Request)} if the notificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notificacions")
    public ResponseEntity<NotificacionDTO> createNotificacion(@Valid @RequestBody NotificacionDTO notificacionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacionDTO);
        if (notificacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificacionDTO result = notificacionService.save(notificacionDTO);
        return ResponseEntity
            .created(new URI("/api/notificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacions/:id} : Updates an existing notificacion.
     *
     * @param id the id of the notificacionDTO to save.
     * @param notificacionDTO the notificacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacionDTO,
     * or with status {@code 400 (Bad Request)} if the notificacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notificacions/{id}")
    public ResponseEntity<NotificacionDTO> updateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificacionDTO notificacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}, {}", id, notificacionDTO);
        if (notificacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificacionDTO result = notificacionService.update(notificacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notificacions/:id} : Partial updates given fields of an existing notificacion, field will ignore if it is null
     *
     * @param id the id of the notificacionDTO to save.
     * @param notificacionDTO the notificacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacionDTO,
     * or with status {@code 400 (Bad Request)} if the notificacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notificacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificacionDTO> partialUpdateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificacionDTO notificacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notificacion partially : {}, {}", id, notificacionDTO);
        if (notificacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificacionDTO> result = notificacionService.partialUpdate(notificacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacions} : get all the notificacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacions in body.
     */
    @GetMapping("/notificacions")
    public ResponseEntity<List<NotificacionDTO>> getAllNotificacions(NotificacionCriteria criteria) {
        log.debug("REST request to get Notificacions by criteria: {}", criteria);
        List<NotificacionDTO> entityList = notificacionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /notificacions/count} : count all the notificacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notificacions/count")
    public ResponseEntity<Long> countNotificacions(NotificacionCriteria criteria) {
        log.debug("REST request to count Notificacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notificacions/:id} : get the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/{id}")
    public ResponseEntity<NotificacionDTO> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Optional<NotificacionDTO> notificacionDTO = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificacionDTO);
    }

    /**
     * {@code DELETE  /notificacions/:id} : delete the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notificacions/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/notificacions/read")
    public ResponseEntity<NotificacionDTO> readNotificacion(@Valid @RequestBody Long id) {
        Optional<Notificacion> notif = notificacionRepository.findById(id);
        if (!notif.isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Notificacion notif2 = notif.get();
        notif2.setStatus(Status.LEIDO);
        notif2 = notificacionRepository.save(notif2);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notif2.getId().toString()))
            .body(notificacionMapper.toDto(notif2));
    }
}
