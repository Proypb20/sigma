package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Notificacion;
import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.domain.enumeration.Status;
import com.sigma.myapp.repository.NotificacionRepository;
import com.sigma.myapp.service.criteria.NotificacionCriteria;
import com.sigma.myapp.service.dto.NotificacionDTO;
import com.sigma.myapp.service.mapper.NotificacionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificacionResourceIT {

    private static final Status DEFAULT_STATUS = Status.PENDIENTE;
    private static final Status UPDATED_STATUS = Status.LEIDO;

    private static final String ENTITY_API_URL = "/api/notificacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private NotificacionMapper notificacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacionMockMvc;

    private Notificacion notificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion().status(DEFAULT_STATUS);
        // Add required entity
        Novedad novedad;
        if (TestUtil.findAll(em, Novedad.class).isEmpty()) {
            novedad = NovedadResourceIT.createEntity(em);
            em.persist(novedad);
            em.flush();
        } else {
            novedad = TestUtil.findAll(em, Novedad.class).get(0);
        }
        notificacion.setNovedad(novedad);
        // Add required entity
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            vigilador = VigiladorResourceIT.createEntity(em);
            em.persist(vigilador);
            em.flush();
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        notificacion.setVigilador(vigilador);
        return notificacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createUpdatedEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion().status(UPDATED_STATUS);
        // Add required entity
        Novedad novedad;
        if (TestUtil.findAll(em, Novedad.class).isEmpty()) {
            novedad = NovedadResourceIT.createUpdatedEntity(em);
            em.persist(novedad);
            em.flush();
        } else {
            novedad = TestUtil.findAll(em, Novedad.class).get(0);
        }
        notificacion.setNovedad(novedad);
        // Add required entity
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            vigilador = VigiladorResourceIT.createUpdatedEntity(em);
            em.persist(vigilador);
            em.flush();
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        notificacion.setVigilador(vigilador);
        return notificacion;
    }

    @BeforeEach
    public void initTest() {
        notificacion = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificacion() throws Exception {
        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();
        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate + 1);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createNotificacionWithExistingId() throws Exception {
        // Create the Notificacion with an existing ID
        notificacion.setId(1L);
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacionRepository.findAll().size();
        // set the field null
        notificacion.setStatus(null);

        // Create the Notificacion, which fails.
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificacions() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get the notificacion
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacion.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNotificacionsByIdFiltering() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        Long id = notificacion.getId();

        defaultNotificacionShouldBeFound("id.equals=" + id);
        defaultNotificacionShouldNotBeFound("id.notEquals=" + id);

        defaultNotificacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificacionShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificacionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where status equals to DEFAULT_STATUS
        defaultNotificacionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the notificacionList where status equals to UPDATED_STATUS
        defaultNotificacionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificacionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultNotificacionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the notificacionList where status equals to UPDATED_STATUS
        defaultNotificacionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificacionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where status is not null
        defaultNotificacionShouldBeFound("status.specified=true");

        // Get all the notificacionList where status is null
        defaultNotificacionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacionsByNovedadIsEqualToSomething() throws Exception {
        Novedad novedad;
        if (TestUtil.findAll(em, Novedad.class).isEmpty()) {
            notificacionRepository.saveAndFlush(notificacion);
            novedad = NovedadResourceIT.createEntity(em);
        } else {
            novedad = TestUtil.findAll(em, Novedad.class).get(0);
        }
        em.persist(novedad);
        em.flush();
        notificacion.setNovedad(novedad);
        notificacionRepository.saveAndFlush(notificacion);
        Long novedadId = novedad.getId();

        // Get all the notificacionList where novedad equals to novedadId
        defaultNotificacionShouldBeFound("novedadId.equals=" + novedadId);

        // Get all the notificacionList where novedad equals to (novedadId + 1)
        defaultNotificacionShouldNotBeFound("novedadId.equals=" + (novedadId + 1));
    }

    @Test
    @Transactional
    void getAllNotificacionsByVigiladorIsEqualToSomething() throws Exception {
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            notificacionRepository.saveAndFlush(notificacion);
            vigilador = VigiladorResourceIT.createEntity(em);
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        em.persist(vigilador);
        em.flush();
        notificacion.setVigilador(vigilador);
        notificacionRepository.saveAndFlush(notificacion);
        Long vigiladorId = vigilador.getId();

        // Get all the notificacionList where vigilador equals to vigiladorId
        defaultNotificacionShouldBeFound("vigiladorId.equals=" + vigiladorId);

        // Get all the notificacionList where vigilador equals to (vigiladorId + 1)
        defaultNotificacionShouldNotBeFound("vigiladorId.equals=" + (vigiladorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificacionShouldBeFound(String filter) throws Exception {
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificacionShouldNotBeFound(String filter) throws Exception {
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificacion() throws Exception {
        // Get the notificacion
        restNotificacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion
        Notificacion updatedNotificacion = notificacionRepository.findById(notificacion.getId()).get();
        // Disconnect from session so that the updates on updatedNotificacion are not directly saved in db
        em.detach(updatedNotificacion);
        updatedNotificacion.status(UPDATED_STATUS);
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(updatedNotificacion);

        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        partialUpdatedNotificacion.status(UPDATED_STATUS);

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeDelete = notificacionRepository.findAll().size();

        // Delete the notificacion
        restNotificacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
