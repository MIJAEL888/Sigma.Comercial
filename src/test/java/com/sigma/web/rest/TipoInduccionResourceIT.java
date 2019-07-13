package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.TipoInduccion;
import com.sigma.repository.TipoInduccionRepository;
import com.sigma.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TipoInduccionResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class TipoInduccionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoInduccionRepository tipoInduccionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTipoInduccionMockMvc;

    private TipoInduccion tipoInduccion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoInduccionResource tipoInduccionResource = new TipoInduccionResource(tipoInduccionRepository);
        this.restTipoInduccionMockMvc = MockMvcBuilders.standaloneSetup(tipoInduccionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoInduccion createEntity(EntityManager em) {
        TipoInduccion tipoInduccion = new TipoInduccion()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoInduccion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoInduccion createUpdatedEntity(EntityManager em) {
        TipoInduccion tipoInduccion = new TipoInduccion()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return tipoInduccion;
    }

    @BeforeEach
    public void initTest() {
        tipoInduccion = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoInduccion() throws Exception {
        int databaseSizeBeforeCreate = tipoInduccionRepository.findAll().size();

        // Create the TipoInduccion
        restTipoInduccionMockMvc.perform(post("/api/tipo-induccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoInduccion)))
            .andExpect(status().isCreated());

        // Validate the TipoInduccion in the database
        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeCreate + 1);
        TipoInduccion testTipoInduccion = tipoInduccionList.get(tipoInduccionList.size() - 1);
        assertThat(testTipoInduccion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoInduccion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createTipoInduccionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoInduccionRepository.findAll().size();

        // Create the TipoInduccion with an existing ID
        tipoInduccion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoInduccionMockMvc.perform(post("/api/tipo-induccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoInduccion)))
            .andExpect(status().isBadRequest());

        // Validate the TipoInduccion in the database
        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoInduccionRepository.findAll().size();
        // set the field null
        tipoInduccion.setNombre(null);

        // Create the TipoInduccion, which fails.

        restTipoInduccionMockMvc.perform(post("/api/tipo-induccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoInduccion)))
            .andExpect(status().isBadRequest());

        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoInduccions() throws Exception {
        // Initialize the database
        tipoInduccionRepository.saveAndFlush(tipoInduccion);

        // Get all the tipoInduccionList
        restTipoInduccionMockMvc.perform(get("/api/tipo-induccions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoInduccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getTipoInduccion() throws Exception {
        // Initialize the database
        tipoInduccionRepository.saveAndFlush(tipoInduccion);

        // Get the tipoInduccion
        restTipoInduccionMockMvc.perform(get("/api/tipo-induccions/{id}", tipoInduccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoInduccion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoInduccion() throws Exception {
        // Get the tipoInduccion
        restTipoInduccionMockMvc.perform(get("/api/tipo-induccions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoInduccion() throws Exception {
        // Initialize the database
        tipoInduccionRepository.saveAndFlush(tipoInduccion);

        int databaseSizeBeforeUpdate = tipoInduccionRepository.findAll().size();

        // Update the tipoInduccion
        TipoInduccion updatedTipoInduccion = tipoInduccionRepository.findById(tipoInduccion.getId()).get();
        // Disconnect from session so that the updates on updatedTipoInduccion are not directly saved in db
        em.detach(updatedTipoInduccion);
        updatedTipoInduccion
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restTipoInduccionMockMvc.perform(put("/api/tipo-induccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoInduccion)))
            .andExpect(status().isOk());

        // Validate the TipoInduccion in the database
        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeUpdate);
        TipoInduccion testTipoInduccion = tipoInduccionList.get(tipoInduccionList.size() - 1);
        assertThat(testTipoInduccion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoInduccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoInduccion() throws Exception {
        int databaseSizeBeforeUpdate = tipoInduccionRepository.findAll().size();

        // Create the TipoInduccion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoInduccionMockMvc.perform(put("/api/tipo-induccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoInduccion)))
            .andExpect(status().isBadRequest());

        // Validate the TipoInduccion in the database
        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoInduccion() throws Exception {
        // Initialize the database
        tipoInduccionRepository.saveAndFlush(tipoInduccion);

        int databaseSizeBeforeDelete = tipoInduccionRepository.findAll().size();

        // Delete the tipoInduccion
        restTipoInduccionMockMvc.perform(delete("/api/tipo-induccions/{id}", tipoInduccion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoInduccion> tipoInduccionList = tipoInduccionRepository.findAll();
        assertThat(tipoInduccionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoInduccion.class);
        TipoInduccion tipoInduccion1 = new TipoInduccion();
        tipoInduccion1.setId(1L);
        TipoInduccion tipoInduccion2 = new TipoInduccion();
        tipoInduccion2.setId(tipoInduccion1.getId());
        assertThat(tipoInduccion1).isEqualTo(tipoInduccion2);
        tipoInduccion2.setId(2L);
        assertThat(tipoInduccion1).isNotEqualTo(tipoInduccion2);
        tipoInduccion1.setId(null);
        assertThat(tipoInduccion1).isNotEqualTo(tipoInduccion2);
    }
}
