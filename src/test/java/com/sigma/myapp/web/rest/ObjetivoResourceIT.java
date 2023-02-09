package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.repository.ObjetivoRepository;
import com.sigma.myapp.service.criteria.ObjetivoCriteria;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import com.sigma.myapp.service.mapper.ObjetivoMapper;
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
 * Integration tests for the {@link ObjetivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjetivoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/objetivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private ObjetivoMapper objetivoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjetivoMockMvc;

    private Objetivo objetivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objetivo createEntity(EntityManager em) {
        Objetivo objetivo = new Objetivo().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).city(DEFAULT_CITY);
        return objetivo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objetivo createUpdatedEntity(EntityManager em) {
        Objetivo objetivo = new Objetivo().name(UPDATED_NAME).address(UPDATED_ADDRESS).city(UPDATED_CITY);
        return objetivo;
    }

    @BeforeEach
    public void initTest() {
        objetivo = createEntity(em);
    }

    @Test
    @Transactional
    void createObjetivo() throws Exception {
        int databaseSizeBeforeCreate = objetivoRepository.findAll().size();
        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);
        restObjetivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objetivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeCreate + 1);
        Objetivo testObjetivo = objetivoList.get(objetivoList.size() - 1);
        assertThat(testObjetivo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjetivo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testObjetivo.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    void createObjetivoWithExistingId() throws Exception {
        // Create the Objetivo with an existing ID
        objetivo.setId(1L);
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        int databaseSizeBeforeCreate = objetivoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjetivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objetivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objetivoRepository.findAll().size();
        // set the field null
        objetivo.setName(null);

        // Create the Objetivo, which fails.
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        restObjetivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objetivoDTO)))
            .andExpect(status().isBadRequest());

        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllObjetivos() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objetivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    @Transactional
    void getObjetivo() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get the objetivo
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL_ID, objetivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objetivo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    void getObjetivosByIdFiltering() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        Long id = objetivo.getId();

        defaultObjetivoShouldBeFound("id.equals=" + id);
        defaultObjetivoShouldNotBeFound("id.notEquals=" + id);

        defaultObjetivoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultObjetivoShouldNotBeFound("id.greaterThan=" + id);

        defaultObjetivoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultObjetivoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllObjetivosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where name equals to DEFAULT_NAME
        defaultObjetivoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the objetivoList where name equals to UPDATED_NAME
        defaultObjetivoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllObjetivosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultObjetivoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the objetivoList where name equals to UPDATED_NAME
        defaultObjetivoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllObjetivosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where name is not null
        defaultObjetivoShouldBeFound("name.specified=true");

        // Get all the objetivoList where name is null
        defaultObjetivoShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllObjetivosByNameContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where name contains DEFAULT_NAME
        defaultObjetivoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the objetivoList where name contains UPDATED_NAME
        defaultObjetivoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllObjetivosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where name does not contain DEFAULT_NAME
        defaultObjetivoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the objetivoList where name does not contain UPDATED_NAME
        defaultObjetivoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllObjetivosByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where address equals to DEFAULT_ADDRESS
        defaultObjetivoShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the objetivoList where address equals to UPDATED_ADDRESS
        defaultObjetivoShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllObjetivosByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultObjetivoShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the objetivoList where address equals to UPDATED_ADDRESS
        defaultObjetivoShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllObjetivosByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where address is not null
        defaultObjetivoShouldBeFound("address.specified=true");

        // Get all the objetivoList where address is null
        defaultObjetivoShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllObjetivosByAddressContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where address contains DEFAULT_ADDRESS
        defaultObjetivoShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the objetivoList where address contains UPDATED_ADDRESS
        defaultObjetivoShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllObjetivosByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where address does not contain DEFAULT_ADDRESS
        defaultObjetivoShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the objetivoList where address does not contain UPDATED_ADDRESS
        defaultObjetivoShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllObjetivosByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where city equals to DEFAULT_CITY
        defaultObjetivoShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the objetivoList where city equals to UPDATED_CITY
        defaultObjetivoShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllObjetivosByCityIsInShouldWork() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where city in DEFAULT_CITY or UPDATED_CITY
        defaultObjetivoShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the objetivoList where city equals to UPDATED_CITY
        defaultObjetivoShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllObjetivosByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where city is not null
        defaultObjetivoShouldBeFound("city.specified=true");

        // Get all the objetivoList where city is null
        defaultObjetivoShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllObjetivosByCityContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where city contains DEFAULT_CITY
        defaultObjetivoShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the objetivoList where city contains UPDATED_CITY
        defaultObjetivoShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllObjetivosByCityNotContainsSomething() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        // Get all the objetivoList where city does not contain DEFAULT_CITY
        defaultObjetivoShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the objetivoList where city does not contain UPDATED_CITY
        defaultObjetivoShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultObjetivoShouldBeFound(String filter) throws Exception {
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objetivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));

        // Check, that the count call also returns 1
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultObjetivoShouldNotBeFound(String filter) throws Exception {
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restObjetivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingObjetivo() throws Exception {
        // Get the objetivo
        restObjetivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingObjetivo() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();

        // Update the objetivo
        Objetivo updatedObjetivo = objetivoRepository.findById(objetivo.getId()).get();
        // Disconnect from session so that the updates on updatedObjetivo are not directly saved in db
        em.detach(updatedObjetivo);
        updatedObjetivo.name(UPDATED_NAME).address(UPDATED_ADDRESS).city(UPDATED_CITY);
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(updatedObjetivo);

        restObjetivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objetivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
        Objetivo testObjetivo = objetivoList.get(objetivoList.size() - 1);
        assertThat(testObjetivo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjetivo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testObjetivo.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void putNonExistingObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objetivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objetivoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjetivoWithPatch() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();

        // Update the objetivo using partial update
        Objetivo partialUpdatedObjetivo = new Objetivo();
        partialUpdatedObjetivo.setId(objetivo.getId());

        partialUpdatedObjetivo.name(UPDATED_NAME);

        restObjetivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjetivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjetivo))
            )
            .andExpect(status().isOk());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
        Objetivo testObjetivo = objetivoList.get(objetivoList.size() - 1);
        assertThat(testObjetivo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjetivo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testObjetivo.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    void fullUpdateObjetivoWithPatch() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();

        // Update the objetivo using partial update
        Objetivo partialUpdatedObjetivo = new Objetivo();
        partialUpdatedObjetivo.setId(objetivo.getId());

        partialUpdatedObjetivo.name(UPDATED_NAME).address(UPDATED_ADDRESS).city(UPDATED_CITY);

        restObjetivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjetivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjetivo))
            )
            .andExpect(status().isOk());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
        Objetivo testObjetivo = objetivoList.get(objetivoList.size() - 1);
        assertThat(testObjetivo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjetivo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testObjetivo.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void patchNonExistingObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objetivoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjetivo() throws Exception {
        int databaseSizeBeforeUpdate = objetivoRepository.findAll().size();
        objetivo.setId(count.incrementAndGet());

        // Create the Objetivo
        ObjetivoDTO objetivoDTO = objetivoMapper.toDto(objetivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjetivoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(objetivoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objetivo in the database
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjetivo() throws Exception {
        // Initialize the database
        objetivoRepository.saveAndFlush(objetivo);

        int databaseSizeBeforeDelete = objetivoRepository.findAll().size();

        // Delete the objetivo
        restObjetivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, objetivo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Objetivo> objetivoList = objetivoRepository.findAll();
        assertThat(objetivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
