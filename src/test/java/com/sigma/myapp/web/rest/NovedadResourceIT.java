package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.domain.enumeration.Entregar;
import com.sigma.myapp.repository.NovedadRepository;
import com.sigma.myapp.service.criteria.NovedadCriteria;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.service.mapper.NovedadMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link NovedadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NovedadResourceIT {

    private static final String DEFAULT_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Entregar DEFAULT_ENTREGAR = Entregar.VIGILADOR;
    private static final Entregar UPDATED_ENTREGAR = Entregar.OBJETIVO;

    private static final String ENTITY_API_URL = "/api/novedads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NovedadRepository novedadRepository;

    @Autowired
    private NovedadMapper novedadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNovedadMockMvc;

    private Novedad novedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novedad createEntity(EntityManager em) {
        Novedad novedad = new Novedad()
            .texto(DEFAULT_TEXTO)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .entregar(DEFAULT_ENTREGAR);
        return novedad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novedad createUpdatedEntity(EntityManager em) {
        Novedad novedad = new Novedad()
            .texto(UPDATED_TEXTO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .entregar(UPDATED_ENTREGAR);
        return novedad;
    }

    @BeforeEach
    public void initTest() {
        novedad = createEntity(em);
    }

    @Test
    @Transactional
    void createNovedad() throws Exception {
        int databaseSizeBeforeCreate = novedadRepository.findAll().size();
        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);
        restNovedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(novedadDTO)))
            .andExpect(status().isCreated());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeCreate + 1);
        Novedad testNovedad = novedadList.get(novedadList.size() - 1);
        assertThat(testNovedad.getTexto()).isEqualTo(DEFAULT_TEXTO);
        assertThat(testNovedad.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testNovedad.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testNovedad.getEntregar()).isEqualTo(DEFAULT_ENTREGAR);
    }

    @Test
    @Transactional
    void createNovedadWithExistingId() throws Exception {
        // Create the Novedad with an existing ID
        novedad.setId(1L);
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        int databaseSizeBeforeCreate = novedadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNovedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(novedadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextoIsRequired() throws Exception {
        int databaseSizeBeforeTest = novedadRepository.findAll().size();
        // set the field null
        novedad.setTexto(null);

        // Create the Novedad, which fails.
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        restNovedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(novedadDTO)))
            .andExpect(status().isBadRequest());

        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntregarIsRequired() throws Exception {
        int databaseSizeBeforeTest = novedadRepository.findAll().size();
        // set the field null
        novedad.setEntregar(null);

        // Create the Novedad, which fails.
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        restNovedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(novedadDTO)))
            .andExpect(status().isBadRequest());

        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNovedads() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(novedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].entregar").value(hasItem(DEFAULT_ENTREGAR.toString())));
    }

    @Test
    @Transactional
    void getNovedad() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get the novedad
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL_ID, novedad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(novedad.getId().intValue()))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.entregar").value(DEFAULT_ENTREGAR.toString()));
    }

    @Test
    @Transactional
    void getNovedadsByIdFiltering() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        Long id = novedad.getId();

        defaultNovedadShouldBeFound("id.equals=" + id);
        defaultNovedadShouldNotBeFound("id.notEquals=" + id);

        defaultNovedadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNovedadShouldNotBeFound("id.greaterThan=" + id);

        defaultNovedadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNovedadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNovedadsByTextoIsEqualToSomething() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where texto equals to DEFAULT_TEXTO
        defaultNovedadShouldBeFound("texto.equals=" + DEFAULT_TEXTO);

        // Get all the novedadList where texto equals to UPDATED_TEXTO
        defaultNovedadShouldNotBeFound("texto.equals=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllNovedadsByTextoIsInShouldWork() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where texto in DEFAULT_TEXTO or UPDATED_TEXTO
        defaultNovedadShouldBeFound("texto.in=" + DEFAULT_TEXTO + "," + UPDATED_TEXTO);

        // Get all the novedadList where texto equals to UPDATED_TEXTO
        defaultNovedadShouldNotBeFound("texto.in=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllNovedadsByTextoIsNullOrNotNull() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where texto is not null
        defaultNovedadShouldBeFound("texto.specified=true");

        // Get all the novedadList where texto is null
        defaultNovedadShouldNotBeFound("texto.specified=false");
    }

    @Test
    @Transactional
    void getAllNovedadsByTextoContainsSomething() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where texto contains DEFAULT_TEXTO
        defaultNovedadShouldBeFound("texto.contains=" + DEFAULT_TEXTO);

        // Get all the novedadList where texto contains UPDATED_TEXTO
        defaultNovedadShouldNotBeFound("texto.contains=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllNovedadsByTextoNotContainsSomething() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where texto does not contain DEFAULT_TEXTO
        defaultNovedadShouldNotBeFound("texto.doesNotContain=" + DEFAULT_TEXTO);

        // Get all the novedadList where texto does not contain UPDATED_TEXTO
        defaultNovedadShouldBeFound("texto.doesNotContain=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllNovedadsByEntregarIsEqualToSomething() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where entregar equals to DEFAULT_ENTREGAR
        defaultNovedadShouldBeFound("entregar.equals=" + DEFAULT_ENTREGAR);

        // Get all the novedadList where entregar equals to UPDATED_ENTREGAR
        defaultNovedadShouldNotBeFound("entregar.equals=" + UPDATED_ENTREGAR);
    }

    @Test
    @Transactional
    void getAllNovedadsByEntregarIsInShouldWork() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where entregar in DEFAULT_ENTREGAR or UPDATED_ENTREGAR
        defaultNovedadShouldBeFound("entregar.in=" + DEFAULT_ENTREGAR + "," + UPDATED_ENTREGAR);

        // Get all the novedadList where entregar equals to UPDATED_ENTREGAR
        defaultNovedadShouldNotBeFound("entregar.in=" + UPDATED_ENTREGAR);
    }

    @Test
    @Transactional
    void getAllNovedadsByEntregarIsNullOrNotNull() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        // Get all the novedadList where entregar is not null
        defaultNovedadShouldBeFound("entregar.specified=true");

        // Get all the novedadList where entregar is null
        defaultNovedadShouldNotBeFound("entregar.specified=false");
    }

    @Test
    @Transactional
    void getAllNovedadsByVigiladorIsEqualToSomething() throws Exception {
        Vigilador vigilador;
        if (TestUtil.findAll(em, Vigilador.class).isEmpty()) {
            novedadRepository.saveAndFlush(novedad);
            vigilador = VigiladorResourceIT.createEntity(em);
        } else {
            vigilador = TestUtil.findAll(em, Vigilador.class).get(0);
        }
        em.persist(vigilador);
        em.flush();
        novedad.setVigilador(vigilador);
        novedadRepository.saveAndFlush(novedad);
        Long vigiladorId = vigilador.getId();

        // Get all the novedadList where vigilador equals to vigiladorId
        defaultNovedadShouldBeFound("vigiladorId.equals=" + vigiladorId);

        // Get all the novedadList where vigilador equals to (vigiladorId + 1)
        defaultNovedadShouldNotBeFound("vigiladorId.equals=" + (vigiladorId + 1));
    }

    @Test
    @Transactional
    void getAllNovedadsByObjetivoIsEqualToSomething() throws Exception {
        Objetivo objetivo;
        if (TestUtil.findAll(em, Objetivo.class).isEmpty()) {
            novedadRepository.saveAndFlush(novedad);
            objetivo = ObjetivoResourceIT.createEntity(em);
        } else {
            objetivo = TestUtil.findAll(em, Objetivo.class).get(0);
        }
        em.persist(objetivo);
        em.flush();
        novedad.setObjetivo(objetivo);
        novedadRepository.saveAndFlush(novedad);
        Long objetivoId = objetivo.getId();

        // Get all the novedadList where objetivo equals to objetivoId
        defaultNovedadShouldBeFound("objetivoId.equals=" + objetivoId);

        // Get all the novedadList where objetivo equals to (objetivoId + 1)
        defaultNovedadShouldNotBeFound("objetivoId.equals=" + (objetivoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNovedadShouldBeFound(String filter) throws Exception {
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(novedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].entregar").value(hasItem(DEFAULT_ENTREGAR.toString())));

        // Check, that the count call also returns 1
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNovedadShouldNotBeFound(String filter) throws Exception {
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNovedadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNovedad() throws Exception {
        // Get the novedad
        restNovedadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNovedad() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();

        // Update the novedad
        Novedad updatedNovedad = novedadRepository.findById(novedad.getId()).get();
        // Disconnect from session so that the updates on updatedNovedad are not directly saved in db
        em.detach(updatedNovedad);
        updatedNovedad
            .texto(UPDATED_TEXTO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .entregar(UPDATED_ENTREGAR);
        NovedadDTO novedadDTO = novedadMapper.toDto(updatedNovedad);

        restNovedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, novedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
        Novedad testNovedad = novedadList.get(novedadList.size() - 1);
        assertThat(testNovedad.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testNovedad.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testNovedad.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testNovedad.getEntregar()).isEqualTo(UPDATED_ENTREGAR);
    }

    @Test
    @Transactional
    void putNonExistingNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, novedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(novedadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNovedadWithPatch() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();

        // Update the novedad using partial update
        Novedad partialUpdatedNovedad = new Novedad();
        partialUpdatedNovedad.setId(novedad.getId());

        partialUpdatedNovedad
            .texto(UPDATED_TEXTO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .entregar(UPDATED_ENTREGAR);

        restNovedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNovedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNovedad))
            )
            .andExpect(status().isOk());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
        Novedad testNovedad = novedadList.get(novedadList.size() - 1);
        assertThat(testNovedad.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testNovedad.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testNovedad.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testNovedad.getEntregar()).isEqualTo(UPDATED_ENTREGAR);
    }

    @Test
    @Transactional
    void fullUpdateNovedadWithPatch() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();

        // Update the novedad using partial update
        Novedad partialUpdatedNovedad = new Novedad();
        partialUpdatedNovedad.setId(novedad.getId());

        partialUpdatedNovedad
            .texto(UPDATED_TEXTO)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .entregar(UPDATED_ENTREGAR);

        restNovedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNovedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNovedad))
            )
            .andExpect(status().isOk());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
        Novedad testNovedad = novedadList.get(novedadList.size() - 1);
        assertThat(testNovedad.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testNovedad.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testNovedad.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testNovedad.getEntregar()).isEqualTo(UPDATED_ENTREGAR);
    }

    @Test
    @Transactional
    void patchNonExistingNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, novedadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNovedad() throws Exception {
        int databaseSizeBeforeUpdate = novedadRepository.findAll().size();
        novedad.setId(count.incrementAndGet());

        // Create the Novedad
        NovedadDTO novedadDTO = novedadMapper.toDto(novedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNovedadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(novedadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Novedad in the database
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNovedad() throws Exception {
        // Initialize the database
        novedadRepository.saveAndFlush(novedad);

        int databaseSizeBeforeDelete = novedadRepository.findAll().size();

        // Delete the novedad
        restNovedadMockMvc
            .perform(delete(ENTITY_API_URL_ID, novedad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Novedad> novedadList = novedadRepository.findAll();
        assertThat(novedadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
