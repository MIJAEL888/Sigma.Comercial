package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.Distrito;
import com.sigma.repository.DistritoRepository;
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
 * Integration tests for the {@Link DistritoResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class DistritoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_UBIGEO = "AAAAAAAAAA";
    private static final String UPDATED_UBIGEO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private DistritoRepository distritoRepository;

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

    private MockMvc restDistritoMockMvc;

    private Distrito distrito;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistritoResource distritoResource = new DistritoResource(distritoRepository);
        this.restDistritoMockMvc = MockMvcBuilders.standaloneSetup(distritoResource)
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
    public static Distrito createEntity(EntityManager em) {
        Distrito distrito = new Distrito()
            .nombre(DEFAULT_NOMBRE)
            .ubigeo(DEFAULT_UBIGEO)
            .descripcion(DEFAULT_DESCRIPCION);
        return distrito;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distrito createUpdatedEntity(EntityManager em) {
        Distrito distrito = new Distrito()
            .nombre(UPDATED_NOMBRE)
            .ubigeo(UPDATED_UBIGEO)
            .descripcion(UPDATED_DESCRIPCION);
        return distrito;
    }

    @BeforeEach
    public void initTest() {
        distrito = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistrito() throws Exception {
        int databaseSizeBeforeCreate = distritoRepository.findAll().size();

        // Create the Distrito
        restDistritoMockMvc.perform(post("/api/distritos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distrito)))
            .andExpect(status().isCreated());

        // Validate the Distrito in the database
        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeCreate + 1);
        Distrito testDistrito = distritoList.get(distritoList.size() - 1);
        assertThat(testDistrito.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDistrito.getUbigeo()).isEqualTo(DEFAULT_UBIGEO);
        assertThat(testDistrito.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createDistritoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = distritoRepository.findAll().size();

        // Create the Distrito with an existing ID
        distrito.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistritoMockMvc.perform(post("/api/distritos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distrito)))
            .andExpect(status().isBadRequest());

        // Validate the Distrito in the database
        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = distritoRepository.findAll().size();
        // set the field null
        distrito.setNombre(null);

        // Create the Distrito, which fails.

        restDistritoMockMvc.perform(post("/api/distritos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distrito)))
            .andExpect(status().isBadRequest());

        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDistritos() throws Exception {
        // Initialize the database
        distritoRepository.saveAndFlush(distrito);

        // Get all the distritoList
        restDistritoMockMvc.perform(get("/api/distritos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distrito.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ubigeo").value(hasItem(DEFAULT_UBIGEO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getDistrito() throws Exception {
        // Initialize the database
        distritoRepository.saveAndFlush(distrito);

        // Get the distrito
        restDistritoMockMvc.perform(get("/api/distritos/{id}", distrito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distrito.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ubigeo").value(DEFAULT_UBIGEO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistrito() throws Exception {
        // Get the distrito
        restDistritoMockMvc.perform(get("/api/distritos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistrito() throws Exception {
        // Initialize the database
        distritoRepository.saveAndFlush(distrito);

        int databaseSizeBeforeUpdate = distritoRepository.findAll().size();

        // Update the distrito
        Distrito updatedDistrito = distritoRepository.findById(distrito.getId()).get();
        // Disconnect from session so that the updates on updatedDistrito are not directly saved in db
        em.detach(updatedDistrito);
        updatedDistrito
            .nombre(UPDATED_NOMBRE)
            .ubigeo(UPDATED_UBIGEO)
            .descripcion(UPDATED_DESCRIPCION);

        restDistritoMockMvc.perform(put("/api/distritos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDistrito)))
            .andExpect(status().isOk());

        // Validate the Distrito in the database
        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeUpdate);
        Distrito testDistrito = distritoList.get(distritoList.size() - 1);
        assertThat(testDistrito.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDistrito.getUbigeo()).isEqualTo(UPDATED_UBIGEO);
        assertThat(testDistrito.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingDistrito() throws Exception {
        int databaseSizeBeforeUpdate = distritoRepository.findAll().size();

        // Create the Distrito

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistritoMockMvc.perform(put("/api/distritos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distrito)))
            .andExpect(status().isBadRequest());

        // Validate the Distrito in the database
        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDistrito() throws Exception {
        // Initialize the database
        distritoRepository.saveAndFlush(distrito);

        int databaseSizeBeforeDelete = distritoRepository.findAll().size();

        // Delete the distrito
        restDistritoMockMvc.perform(delete("/api/distritos/{id}", distrito.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Distrito> distritoList = distritoRepository.findAll();
        assertThat(distritoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distrito.class);
        Distrito distrito1 = new Distrito();
        distrito1.setId(1L);
        Distrito distrito2 = new Distrito();
        distrito2.setId(distrito1.getId());
        assertThat(distrito1).isEqualTo(distrito2);
        distrito2.setId(2L);
        assertThat(distrito1).isNotEqualTo(distrito2);
        distrito1.setId(null);
        assertThat(distrito1).isNotEqualTo(distrito2);
    }
}
