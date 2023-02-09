package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Servicio;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.repository.ServicioRepository;
import com.sigma.myapp.service.criteria.ServicioCriteria;
import com.sigma.myapp.service.dto.ServicioDTO;
import com.sigma.myapp.service.mapper.ServicioMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ServicioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServicioResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ServicioMapper servicioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioMockMvc;

    private Servicio servicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createEntity(EntityManager em) {
        Servicio servicio = new Servicio().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        // Add required entity
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            vigilador = VigiladorResourceIT.createEntity(em);
            em.persist(vigilador);
            em.flush();
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        servicio.setVigilador(vigilador);
        return servicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createUpdatedEntity(EntityManager em) {
        Servicio servicio = new Servicio().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        // Add required entity
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            vigilador = VigiladorResourceIT.createUpdatedEntity(em);
            em.persist(vigilador);
            em.flush();
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        servicio.setVigilador(vigilador);
        return servicio;
    }

    @BeforeEach
    public void initTest() {
        servicio = createEntity(em);
    }

    @Test
    @Transactional
    void createServicio() throws Exception {
        int databaseSizeBeforeCreate = servicioRepository.findAll().size();
        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isCreated());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate + 1);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testServicio.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createServicioWithExistingId() throws Exception {
        // Create the Servicio with an existing ID
        servicio.setId(1L);
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        int databaseSizeBeforeCreate = servicioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setStartDate(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicios() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get the servicio
        restServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, servicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicio.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getServiciosByIdFiltering() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        Long id = servicio.getId();

        defaultServicioShouldBeFound("id.equals=" + id);
        defaultServicioShouldNotBeFound("id.notEquals=" + id);

        defaultServicioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServicioShouldNotBeFound("id.greaterThan=" + id);

        defaultServicioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServicioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiciosByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where startDate equals to DEFAULT_START_DATE
        defaultServicioShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the servicioList where startDate equals to UPDATED_START_DATE
        defaultServicioShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllServiciosByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultServicioShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the servicioList where startDate equals to UPDATED_START_DATE
        defaultServicioShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllServiciosByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where startDate is not null
        defaultServicioShouldBeFound("startDate.specified=true");

        // Get all the servicioList where startDate is null
        defaultServicioShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where endDate equals to DEFAULT_END_DATE
        defaultServicioShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the servicioList where endDate equals to UPDATED_END_DATE
        defaultServicioShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllServiciosByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultServicioShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the servicioList where endDate equals to UPDATED_END_DATE
        defaultServicioShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllServiciosByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where endDate is not null
        defaultServicioShouldBeFound("endDate.specified=true");

        // Get all the servicioList where endDate is null
        defaultServicioShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByVigiladorIsEqualToSomething() throws Exception {
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            servicioRepository.saveAndFlush(servicio);
            vigilador = VigiladorResourceIT.createEntity(em);
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        em.persist(vigilador);
        em.flush();
        servicio.setVigilador(vigilador);
        servicioRepository.saveAndFlush(servicio);
        Long vigiladorId = vigilador.getId();

        // Get all the servicioList where vigilador equals to vigiladorId
        defaultServicioShouldBeFound("vigiladorId.equals=" + vigiladorId);

        // Get all the servicioList where vigilador equals to (vigiladorId + 1)
        defaultServicioShouldNotBeFound("vigiladorId.equals=" + (vigiladorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicioShouldBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicioShouldNotBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicio() throws Exception {
        // Get the servicio
        restServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio
        Servicio updatedServicio = servicioRepository.findById(servicio.getId()).get();
        // Disconnect from session so that the updates on updatedServicio are not directly saved in db
        em.detach(updatedServicio);
        updatedServicio.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        ServicioDTO servicioDTO = servicioMapper.toDto(updatedServicio);

        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testServicio.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio.startDate(UPDATED_START_DATE);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testServicio.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testServicio.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeDelete = servicioRepository.findAll().size();

        // Delete the servicio
        restServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
