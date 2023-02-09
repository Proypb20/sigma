package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Categoria;
import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.domain.User;
import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.repository.VigiladorRepository;
import com.sigma.myapp.service.VigiladorService;
import com.sigma.myapp.service.criteria.VigiladorCriteria;
import com.sigma.myapp.service.dto.VigiladorDTO;
import com.sigma.myapp.service.mapper.VigiladorMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VigiladorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VigiladorResourceIT {

    private static final String ENTITY_API_URL = "/api/vigiladors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VigiladorRepository vigiladorRepository;

    @Mock
    private VigiladorRepository vigiladorRepositoryMock;

    @Autowired
    private VigiladorMapper vigiladorMapper;

    @Mock
    private VigiladorService vigiladorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVigiladorMockMvc;

    private Vigilador vigilador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vigilador createEntity(EntityManager em) {
        Vigilador vigilador = new Vigilador();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        vigilador.setUser(user);
        return vigilador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vigilador createUpdatedEntity(EntityManager em) {
        Vigilador vigilador = new Vigilador();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        vigilador.setUser(user);
        return vigilador;
    }

    @BeforeEach
    public void initTest() {
        vigilador = createEntity(em);
    }

    @Test
    @Transactional
    void createVigilador() throws Exception {
        int databaseSizeBeforeCreate = vigiladorRepository.findAll().size();
        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);
        restVigiladorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vigiladorDTO)))
            .andExpect(status().isCreated());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeCreate + 1);
        Vigilador testVigilador = vigiladorList.get(vigiladorList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        assertThat(testVigilador.getId()).isEqualTo(vigiladorDTO.getUser().getId());
    }

    @Test
    @Transactional
    void createVigiladorWithExistingId() throws Exception {
        // Create the Vigilador with an existing ID
        vigilador.setId(1L);
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        int databaseSizeBeforeCreate = vigiladorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVigiladorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vigiladorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateVigiladorMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);
        int databaseSizeBeforeCreate = vigiladorRepository.findAll().size();

        // Load the vigilador
        Vigilador updatedVigilador = vigiladorRepository.findById(vigilador.getId()).get();
        assertThat(updatedVigilador).isNotNull();
        // Disconnect from session so that the updates on updatedVigilador are not directly saved in db
        em.detach(updatedVigilador);

        // Update the User with new association value
        // updatedVigilador.setUser();
        VigiladorDTO updatedVigiladorDTO = vigiladorMapper.toDto(updatedVigilador);
        assertThat(updatedVigiladorDTO).isNotNull();

        // Update the entity
        restVigiladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVigiladorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVigiladorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeCreate);
        Vigilador testVigilador = vigiladorList.get(vigiladorList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testVigilador.getId()).isEqualTo(testVigilador.getUser().getId());
    }

    @Test
    @Transactional
    void getAllVigiladors() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        // Get all the vigiladorList
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vigilador.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVigiladorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vigiladorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVigiladorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vigiladorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVigiladorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vigiladorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVigiladorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vigiladorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVigilador() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        // Get the vigilador
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL_ID, vigilador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vigilador.getId().intValue()));
    }

    @Test
    @Transactional
    void getVigiladorsByIdFiltering() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        Long id = vigilador.getId();

        defaultVigiladorShouldBeFound("id.equals=" + id);
        defaultVigiladorShouldNotBeFound("id.notEquals=" + id);

        defaultVigiladorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVigiladorShouldNotBeFound("id.greaterThan=" + id);

        defaultVigiladorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVigiladorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVigiladorsByCategoriaIsEqualToSomething() throws Exception {
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            vigiladorRepository.saveAndFlush(vigilador);
            categoria = CategoriaResourceIT.createEntity(em);
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        em.persist(categoria);
        em.flush();
        vigilador.setCategoria(categoria);
        vigiladorRepository.saveAndFlush(vigilador);
        Long categoriaId = categoria.getId();

        // Get all the vigiladorList where categoria equals to categoriaId
        defaultVigiladorShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the vigiladorList where categoria equals to (categoriaId + 1)
        defaultVigiladorShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllVigiladorsByObjetivoIsEqualToSomething() throws Exception {
        Objetivo objetivo;
        if (TestUtil.findAll(em, Objetivo.class).isEmpty()) {
            vigiladorRepository.saveAndFlush(vigilador);
            objetivo = ObjetivoResourceIT.createEntity(em);
        } else {
            objetivo = TestUtil.findAll(em, Objetivo.class).get(0);
        }
        em.persist(objetivo);
        em.flush();
        vigilador.setObjetivo(objetivo);
        vigiladorRepository.saveAndFlush(vigilador);
        Long objetivoId = objetivo.getId();

        // Get all the vigiladorList where objetivo equals to objetivoId
        defaultVigiladorShouldBeFound("objetivoId.equals=" + objetivoId);

        // Get all the vigiladorList where objetivo equals to (objetivoId + 1)
        defaultVigiladorShouldNotBeFound("objetivoId.equals=" + (objetivoId + 1));
    }

    @Test
    @Transactional
    void getAllVigiladorsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = vigilador.getUser();
        vigiladorRepository.saveAndFlush(vigilador);
        Long userId = user.getId();

        // Get all the vigiladorList where user equals to userId
        defaultVigiladorShouldBeFound("userId.equals=" + userId);

        // Get all the vigiladorList where user equals to (userId + 1)
        defaultVigiladorShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVigiladorShouldBeFound(String filter) throws Exception {
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vigilador.getId().intValue())));

        // Check, that the count call also returns 1
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVigiladorShouldNotBeFound(String filter) throws Exception {
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVigiladorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVigilador() throws Exception {
        // Get the vigilador
        restVigiladorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVigilador() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();

        // Update the vigilador
        Vigilador updatedVigilador = vigiladorRepository.findById(vigilador.getId()).get();
        // Disconnect from session so that the updates on updatedVigilador are not directly saved in db
        em.detach(updatedVigilador);
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(updatedVigilador);

        restVigiladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vigiladorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
        Vigilador testVigilador = vigiladorList.get(vigiladorList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vigiladorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vigiladorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVigiladorWithPatch() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();

        // Update the vigilador using partial update
        Vigilador partialUpdatedVigilador = new Vigilador();
        partialUpdatedVigilador.setId(vigilador.getId());

        restVigiladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVigilador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVigilador))
            )
            .andExpect(status().isOk());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
        Vigilador testVigilador = vigiladorList.get(vigiladorList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateVigiladorWithPatch() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();

        // Update the vigilador using partial update
        Vigilador partialUpdatedVigilador = new Vigilador();
        partialUpdatedVigilador.setId(vigilador.getId());

        restVigiladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVigilador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVigilador))
            )
            .andExpect(status().isOk());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
        Vigilador testVigilador = vigiladorList.get(vigiladorList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vigiladorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVigilador() throws Exception {
        int databaseSizeBeforeUpdate = vigiladorRepository.findAll().size();
        vigilador.setId(count.incrementAndGet());

        // Create the Vigilador
        VigiladorDTO vigiladorDTO = vigiladorMapper.toDto(vigilador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVigiladorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vigiladorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vigilador in the database
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVigilador() throws Exception {
        // Initialize the database
        vigiladorRepository.saveAndFlush(vigilador);

        int databaseSizeBeforeDelete = vigiladorRepository.findAll().size();

        // Delete the vigilador
        restVigiladorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vigilador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vigilador> vigiladorList = vigiladorRepository.findAll();
        assertThat(vigiladorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
