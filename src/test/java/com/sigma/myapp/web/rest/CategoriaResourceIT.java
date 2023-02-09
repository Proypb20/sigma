package com.sigma.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.myapp.IntegrationTest;
import com.sigma.myapp.domain.Categoria;
import com.sigma.myapp.repository.CategoriaRepository;
import com.sigma.myapp.service.criteria.CategoriaCriteria;
import com.sigma.myapp.service.dto.CategoriaDTO;
import com.sigma.myapp.service.mapper.CategoriaMapper;
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
 * Integration tests for the {@link CategoriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_HOUR_VALUE = 1D;
    private static final Double UPDATED_HOUR_VALUE = 2D;
    private static final Double SMALLER_HOUR_VALUE = 1D - 1D;

    private static final Double DEFAULT_EXTRA_50 = 1D;
    private static final Double UPDATED_EXTRA_50 = 2D;
    private static final Double SMALLER_EXTRA_50 = 1D - 1D;

    private static final Double DEFAULT_EXTRA_100 = 1D;
    private static final Double UPDATED_EXTRA_100 = 2D;
    private static final Double SMALLER_EXTRA_100 = 1D - 1D;

    private static final Double DEFAULT_TOTAL_HOUR = 1D;
    private static final Double UPDATED_TOTAL_HOUR = 2D;
    private static final Double SMALLER_TOTAL_HOUR = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/categorias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaMockMvc;

    private Categoria categoria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createEntity(EntityManager em) {
        Categoria categoria = new Categoria()
            .name(DEFAULT_NAME)
            .hourValue(DEFAULT_HOUR_VALUE)
            .extra50(DEFAULT_EXTRA_50)
            .extra100(DEFAULT_EXTRA_100)
            .totalHour(DEFAULT_TOTAL_HOUR);
        return categoria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createUpdatedEntity(EntityManager em) {
        Categoria categoria = new Categoria()
            .name(UPDATED_NAME)
            .hourValue(UPDATED_HOUR_VALUE)
            .extra50(UPDATED_EXTRA_50)
            .extra100(UPDATED_EXTRA_100)
            .totalHour(UPDATED_TOTAL_HOUR);
        return categoria;
    }

    @BeforeEach
    public void initTest() {
        categoria = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoria() throws Exception {
        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();
        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isCreated());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate + 1);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategoria.getHourValue()).isEqualTo(DEFAULT_HOUR_VALUE);
        assertThat(testCategoria.getExtra50()).isEqualTo(DEFAULT_EXTRA_50);
        assertThat(testCategoria.getExtra100()).isEqualTo(DEFAULT_EXTRA_100);
        assertThat(testCategoria.getTotalHour()).isEqualTo(DEFAULT_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void createCategoriaWithExistingId() throws Exception {
        // Create the Categoria with an existing ID
        categoria.setId(1L);
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaRepository.findAll().size();
        // set the field null
        categoria.setName(null);

        // Create the Categoria, which fails.
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isBadRequest());

        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorias() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].hourValue").value(hasItem(DEFAULT_HOUR_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].extra50").value(hasItem(DEFAULT_EXTRA_50.doubleValue())))
            .andExpect(jsonPath("$.[*].extra100").value(hasItem(DEFAULT_EXTRA_100.doubleValue())))
            .andExpect(jsonPath("$.[*].totalHour").value(hasItem(DEFAULT_TOTAL_HOUR.doubleValue())));
    }

    @Test
    @Transactional
    void getCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get the categoria
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoria.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.hourValue").value(DEFAULT_HOUR_VALUE.doubleValue()))
            .andExpect(jsonPath("$.extra50").value(DEFAULT_EXTRA_50.doubleValue()))
            .andExpect(jsonPath("$.extra100").value(DEFAULT_EXTRA_100.doubleValue()))
            .andExpect(jsonPath("$.totalHour").value(DEFAULT_TOTAL_HOUR.doubleValue()));
    }

    @Test
    @Transactional
    void getCategoriasByIdFiltering() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        Long id = categoria.getId();

        defaultCategoriaShouldBeFound("id.equals=" + id);
        defaultCategoriaShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where name equals to DEFAULT_NAME
        defaultCategoriaShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoriaList where name equals to UPDATED_NAME
        defaultCategoriaShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoriaShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoriaList where name equals to UPDATED_NAME
        defaultCategoriaShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where name is not null
        defaultCategoriaShouldBeFound("name.specified=true");

        // Get all the categoriaList where name is null
        defaultCategoriaShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByNameContainsSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where name contains DEFAULT_NAME
        defaultCategoriaShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the categoriaList where name contains UPDATED_NAME
        defaultCategoriaShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where name does not contain DEFAULT_NAME
        defaultCategoriaShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the categoriaList where name does not contain UPDATED_NAME
        defaultCategoriaShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue equals to DEFAULT_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.equals=" + DEFAULT_HOUR_VALUE);

        // Get all the categoriaList where hourValue equals to UPDATED_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.equals=" + UPDATED_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue in DEFAULT_HOUR_VALUE or UPDATED_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.in=" + DEFAULT_HOUR_VALUE + "," + UPDATED_HOUR_VALUE);

        // Get all the categoriaList where hourValue equals to UPDATED_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.in=" + UPDATED_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue is not null
        defaultCategoriaShouldBeFound("hourValue.specified=true");

        // Get all the categoriaList where hourValue is null
        defaultCategoriaShouldNotBeFound("hourValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue is greater than or equal to DEFAULT_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.greaterThanOrEqual=" + DEFAULT_HOUR_VALUE);

        // Get all the categoriaList where hourValue is greater than or equal to UPDATED_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.greaterThanOrEqual=" + UPDATED_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue is less than or equal to DEFAULT_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.lessThanOrEqual=" + DEFAULT_HOUR_VALUE);

        // Get all the categoriaList where hourValue is less than or equal to SMALLER_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.lessThanOrEqual=" + SMALLER_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue is less than DEFAULT_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.lessThan=" + DEFAULT_HOUR_VALUE);

        // Get all the categoriaList where hourValue is less than UPDATED_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.lessThan=" + UPDATED_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByHourValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where hourValue is greater than DEFAULT_HOUR_VALUE
        defaultCategoriaShouldNotBeFound("hourValue.greaterThan=" + DEFAULT_HOUR_VALUE);

        // Get all the categoriaList where hourValue is greater than SMALLER_HOUR_VALUE
        defaultCategoriaShouldBeFound("hourValue.greaterThan=" + SMALLER_HOUR_VALUE);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 equals to DEFAULT_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.equals=" + DEFAULT_EXTRA_50);

        // Get all the categoriaList where extra50 equals to UPDATED_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.equals=" + UPDATED_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 in DEFAULT_EXTRA_50 or UPDATED_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.in=" + DEFAULT_EXTRA_50 + "," + UPDATED_EXTRA_50);

        // Get all the categoriaList where extra50 equals to UPDATED_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.in=" + UPDATED_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 is not null
        defaultCategoriaShouldBeFound("extra50.specified=true");

        // Get all the categoriaList where extra50 is null
        defaultCategoriaShouldNotBeFound("extra50.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 is greater than or equal to DEFAULT_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.greaterThanOrEqual=" + DEFAULT_EXTRA_50);

        // Get all the categoriaList where extra50 is greater than or equal to UPDATED_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.greaterThanOrEqual=" + UPDATED_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 is less than or equal to DEFAULT_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.lessThanOrEqual=" + DEFAULT_EXTRA_50);

        // Get all the categoriaList where extra50 is less than or equal to SMALLER_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.lessThanOrEqual=" + SMALLER_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 is less than DEFAULT_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.lessThan=" + DEFAULT_EXTRA_50);

        // Get all the categoriaList where extra50 is less than UPDATED_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.lessThan=" + UPDATED_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra50IsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra50 is greater than DEFAULT_EXTRA_50
        defaultCategoriaShouldNotBeFound("extra50.greaterThan=" + DEFAULT_EXTRA_50);

        // Get all the categoriaList where extra50 is greater than SMALLER_EXTRA_50
        defaultCategoriaShouldBeFound("extra50.greaterThan=" + SMALLER_EXTRA_50);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 equals to DEFAULT_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.equals=" + DEFAULT_EXTRA_100);

        // Get all the categoriaList where extra100 equals to UPDATED_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.equals=" + UPDATED_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 in DEFAULT_EXTRA_100 or UPDATED_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.in=" + DEFAULT_EXTRA_100 + "," + UPDATED_EXTRA_100);

        // Get all the categoriaList where extra100 equals to UPDATED_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.in=" + UPDATED_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 is not null
        defaultCategoriaShouldBeFound("extra100.specified=true");

        // Get all the categoriaList where extra100 is null
        defaultCategoriaShouldNotBeFound("extra100.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 is greater than or equal to DEFAULT_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.greaterThanOrEqual=" + DEFAULT_EXTRA_100);

        // Get all the categoriaList where extra100 is greater than or equal to UPDATED_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.greaterThanOrEqual=" + UPDATED_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 is less than or equal to DEFAULT_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.lessThanOrEqual=" + DEFAULT_EXTRA_100);

        // Get all the categoriaList where extra100 is less than or equal to SMALLER_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.lessThanOrEqual=" + SMALLER_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 is less than DEFAULT_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.lessThan=" + DEFAULT_EXTRA_100);

        // Get all the categoriaList where extra100 is less than UPDATED_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.lessThan=" + UPDATED_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByExtra100IsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where extra100 is greater than DEFAULT_EXTRA_100
        defaultCategoriaShouldNotBeFound("extra100.greaterThan=" + DEFAULT_EXTRA_100);

        // Get all the categoriaList where extra100 is greater than SMALLER_EXTRA_100
        defaultCategoriaShouldBeFound("extra100.greaterThan=" + SMALLER_EXTRA_100);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour equals to DEFAULT_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.equals=" + DEFAULT_TOTAL_HOUR);

        // Get all the categoriaList where totalHour equals to UPDATED_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.equals=" + UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour in DEFAULT_TOTAL_HOUR or UPDATED_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.in=" + DEFAULT_TOTAL_HOUR + "," + UPDATED_TOTAL_HOUR);

        // Get all the categoriaList where totalHour equals to UPDATED_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.in=" + UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour is not null
        defaultCategoriaShouldBeFound("totalHour.specified=true");

        // Get all the categoriaList where totalHour is null
        defaultCategoriaShouldNotBeFound("totalHour.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour is greater than or equal to DEFAULT_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.greaterThanOrEqual=" + DEFAULT_TOTAL_HOUR);

        // Get all the categoriaList where totalHour is greater than or equal to UPDATED_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.greaterThanOrEqual=" + UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour is less than or equal to DEFAULT_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.lessThanOrEqual=" + DEFAULT_TOTAL_HOUR);

        // Get all the categoriaList where totalHour is less than or equal to SMALLER_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.lessThanOrEqual=" + SMALLER_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour is less than DEFAULT_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.lessThan=" + DEFAULT_TOTAL_HOUR);

        // Get all the categoriaList where totalHour is less than UPDATED_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.lessThan=" + UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void getAllCategoriasByTotalHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where totalHour is greater than DEFAULT_TOTAL_HOUR
        defaultCategoriaShouldNotBeFound("totalHour.greaterThan=" + DEFAULT_TOTAL_HOUR);

        // Get all the categoriaList where totalHour is greater than SMALLER_TOTAL_HOUR
        defaultCategoriaShouldBeFound("totalHour.greaterThan=" + SMALLER_TOTAL_HOUR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaShouldBeFound(String filter) throws Exception {
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].hourValue").value(hasItem(DEFAULT_HOUR_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].extra50").value(hasItem(DEFAULT_EXTRA_50.doubleValue())))
            .andExpect(jsonPath("$.[*].extra100").value(hasItem(DEFAULT_EXTRA_100.doubleValue())))
            .andExpect(jsonPath("$.[*].totalHour").value(hasItem(DEFAULT_TOTAL_HOUR.doubleValue())));

        // Check, that the count call also returns 1
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaShouldNotBeFound(String filter) throws Exception {
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoria() throws Exception {
        // Get the categoria
        restCategoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria
        Categoria updatedCategoria = categoriaRepository.findById(categoria.getId()).get();
        // Disconnect from session so that the updates on updatedCategoria are not directly saved in db
        em.detach(updatedCategoria);
        updatedCategoria
            .name(UPDATED_NAME)
            .hourValue(UPDATED_HOUR_VALUE)
            .extra50(UPDATED_EXTRA_50)
            .extra100(UPDATED_EXTRA_100)
            .totalHour(UPDATED_TOTAL_HOUR);
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(updatedCategoria);

        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoria.getHourValue()).isEqualTo(UPDATED_HOUR_VALUE);
        assertThat(testCategoria.getExtra50()).isEqualTo(UPDATED_EXTRA_50);
        assertThat(testCategoria.getExtra100()).isEqualTo(UPDATED_EXTRA_100);
        assertThat(testCategoria.getTotalHour()).isEqualTo(UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void putNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setId(categoria.getId());

        partialUpdatedCategoria.name(UPDATED_NAME).hourValue(UPDATED_HOUR_VALUE).extra100(UPDATED_EXTRA_100);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoria.getHourValue()).isEqualTo(UPDATED_HOUR_VALUE);
        assertThat(testCategoria.getExtra50()).isEqualTo(DEFAULT_EXTRA_50);
        assertThat(testCategoria.getExtra100()).isEqualTo(UPDATED_EXTRA_100);
        assertThat(testCategoria.getTotalHour()).isEqualTo(DEFAULT_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setId(categoria.getId());

        partialUpdatedCategoria
            .name(UPDATED_NAME)
            .hourValue(UPDATED_HOUR_VALUE)
            .extra50(UPDATED_EXTRA_50)
            .extra100(UPDATED_EXTRA_100)
            .totalHour(UPDATED_TOTAL_HOUR);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoria.getHourValue()).isEqualTo(UPDATED_HOUR_VALUE);
        assertThat(testCategoria.getExtra50()).isEqualTo(UPDATED_EXTRA_50);
        assertThat(testCategoria.getExtra100()).isEqualTo(UPDATED_EXTRA_100);
        assertThat(testCategoria.getTotalHour()).isEqualTo(UPDATED_TOTAL_HOUR);
    }

    @Test
    @Transactional
    void patchNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeDelete = categoriaRepository.findAll().size();

        // Delete the categoria
        restCategoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
