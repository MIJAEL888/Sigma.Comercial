package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.ParamentroMonitoreo;
import com.sigma.repository.ParamentroMonitoreoRepository;
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
 * Integration tests for the {@Link ParamentroMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class ParamentroMonitoreoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_COSTO = 1F;
    private static final Float UPDATED_COSTO = 2F;

    @Autowired
    private ParamentroMonitoreoRepository paramentroMonitoreoRepository;

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

    private MockMvc restParamentroMonitoreoMockMvc;

    private ParamentroMonitoreo paramentroMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParamentroMonitoreoResource paramentroMonitoreoResource = new ParamentroMonitoreoResource(paramentroMonitoreoRepository);
        this.restParamentroMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(paramentroMonitoreoResource)
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
    public static ParamentroMonitoreo createEntity(EntityManager em) {
        ParamentroMonitoreo paramentroMonitoreo = new ParamentroMonitoreo()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .costo(DEFAULT_COSTO);
        return paramentroMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamentroMonitoreo createUpdatedEntity(EntityManager em) {
        ParamentroMonitoreo paramentroMonitoreo = new ParamentroMonitoreo()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .costo(UPDATED_COSTO);
        return paramentroMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        paramentroMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createParamentroMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = paramentroMonitoreoRepository.findAll().size();

        // Create the ParamentroMonitoreo
        restParamentroMonitoreoMockMvc.perform(post("/api/paramentro-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentroMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the ParamentroMonitoreo in the database
        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        ParamentroMonitoreo testParamentroMonitoreo = paramentroMonitoreoList.get(paramentroMonitoreoList.size() - 1);
        assertThat(testParamentroMonitoreo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParamentroMonitoreo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testParamentroMonitoreo.getCosto()).isEqualTo(DEFAULT_COSTO);
    }

    @Test
    @Transactional
    public void createParamentroMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramentroMonitoreoRepository.findAll().size();

        // Create the ParamentroMonitoreo with an existing ID
        paramentroMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamentroMonitoreoMockMvc.perform(post("/api/paramentro-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentroMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the ParamentroMonitoreo in the database
        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = paramentroMonitoreoRepository.findAll().size();
        // set the field null
        paramentroMonitoreo.setNombre(null);

        // Create the ParamentroMonitoreo, which fails.

        restParamentroMonitoreoMockMvc.perform(post("/api/paramentro-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentroMonitoreo)))
            .andExpect(status().isBadRequest());

        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParamentroMonitoreos() throws Exception {
        // Initialize the database
        paramentroMonitoreoRepository.saveAndFlush(paramentroMonitoreo);

        // Get all the paramentroMonitoreoList
        restParamentroMonitoreoMockMvc.perform(get("/api/paramentro-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramentroMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getParamentroMonitoreo() throws Exception {
        // Initialize the database
        paramentroMonitoreoRepository.saveAndFlush(paramentroMonitoreo);

        // Get the paramentroMonitoreo
        restParamentroMonitoreoMockMvc.perform(get("/api/paramentro-monitoreos/{id}", paramentroMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paramentroMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParamentroMonitoreo() throws Exception {
        // Get the paramentroMonitoreo
        restParamentroMonitoreoMockMvc.perform(get("/api/paramentro-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParamentroMonitoreo() throws Exception {
        // Initialize the database
        paramentroMonitoreoRepository.saveAndFlush(paramentroMonitoreo);

        int databaseSizeBeforeUpdate = paramentroMonitoreoRepository.findAll().size();

        // Update the paramentroMonitoreo
        ParamentroMonitoreo updatedParamentroMonitoreo = paramentroMonitoreoRepository.findById(paramentroMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedParamentroMonitoreo are not directly saved in db
        em.detach(updatedParamentroMonitoreo);
        updatedParamentroMonitoreo
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .costo(UPDATED_COSTO);

        restParamentroMonitoreoMockMvc.perform(put("/api/paramentro-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParamentroMonitoreo)))
            .andExpect(status().isOk());

        // Validate the ParamentroMonitoreo in the database
        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        ParamentroMonitoreo testParamentroMonitoreo = paramentroMonitoreoList.get(paramentroMonitoreoList.size() - 1);
        assertThat(testParamentroMonitoreo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParamentroMonitoreo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testParamentroMonitoreo.getCosto()).isEqualTo(UPDATED_COSTO);
    }

    @Test
    @Transactional
    public void updateNonExistingParamentroMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = paramentroMonitoreoRepository.findAll().size();

        // Create the ParamentroMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamentroMonitoreoMockMvc.perform(put("/api/paramentro-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentroMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the ParamentroMonitoreo in the database
        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParamentroMonitoreo() throws Exception {
        // Initialize the database
        paramentroMonitoreoRepository.saveAndFlush(paramentroMonitoreo);

        int databaseSizeBeforeDelete = paramentroMonitoreoRepository.findAll().size();

        // Delete the paramentroMonitoreo
        restParamentroMonitoreoMockMvc.perform(delete("/api/paramentro-monitoreos/{id}", paramentroMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParamentroMonitoreo> paramentroMonitoreoList = paramentroMonitoreoRepository.findAll();
        assertThat(paramentroMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParamentroMonitoreo.class);
        ParamentroMonitoreo paramentroMonitoreo1 = new ParamentroMonitoreo();
        paramentroMonitoreo1.setId(1L);
        ParamentroMonitoreo paramentroMonitoreo2 = new ParamentroMonitoreo();
        paramentroMonitoreo2.setId(paramentroMonitoreo1.getId());
        assertThat(paramentroMonitoreo1).isEqualTo(paramentroMonitoreo2);
        paramentroMonitoreo2.setId(2L);
        assertThat(paramentroMonitoreo1).isNotEqualTo(paramentroMonitoreo2);
        paramentroMonitoreo1.setId(null);
        assertThat(paramentroMonitoreo1).isNotEqualTo(paramentroMonitoreo2);
    }
}
