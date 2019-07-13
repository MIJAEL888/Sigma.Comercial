package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.RequisitosSeguridad;
import com.sigma.repository.RequisitosSeguridadRepository;
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
 * Integration tests for the {@Link RequisitosSeguridadResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class RequisitosSeguridadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private RequisitosSeguridadRepository requisitosSeguridadRepository;

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

    private MockMvc restRequisitosSeguridadMockMvc;

    private RequisitosSeguridad requisitosSeguridad;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequisitosSeguridadResource requisitosSeguridadResource = new RequisitosSeguridadResource(requisitosSeguridadRepository);
        this.restRequisitosSeguridadMockMvc = MockMvcBuilders.standaloneSetup(requisitosSeguridadResource)
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
    public static RequisitosSeguridad createEntity(EntityManager em) {
        RequisitosSeguridad requisitosSeguridad = new RequisitosSeguridad()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return requisitosSeguridad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitosSeguridad createUpdatedEntity(EntityManager em) {
        RequisitosSeguridad requisitosSeguridad = new RequisitosSeguridad()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return requisitosSeguridad;
    }

    @BeforeEach
    public void initTest() {
        requisitosSeguridad = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequisitosSeguridad() throws Exception {
        int databaseSizeBeforeCreate = requisitosSeguridadRepository.findAll().size();

        // Create the RequisitosSeguridad
        restRequisitosSeguridadMockMvc.perform(post("/api/requisitos-seguridads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requisitosSeguridad)))
            .andExpect(status().isCreated());

        // Validate the RequisitosSeguridad in the database
        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeCreate + 1);
        RequisitosSeguridad testRequisitosSeguridad = requisitosSeguridadList.get(requisitosSeguridadList.size() - 1);
        assertThat(testRequisitosSeguridad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testRequisitosSeguridad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createRequisitosSeguridadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requisitosSeguridadRepository.findAll().size();

        // Create the RequisitosSeguridad with an existing ID
        requisitosSeguridad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequisitosSeguridadMockMvc.perform(post("/api/requisitos-seguridads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requisitosSeguridad)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitosSeguridad in the database
        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = requisitosSeguridadRepository.findAll().size();
        // set the field null
        requisitosSeguridad.setNombre(null);

        // Create the RequisitosSeguridad, which fails.

        restRequisitosSeguridadMockMvc.perform(post("/api/requisitos-seguridads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requisitosSeguridad)))
            .andExpect(status().isBadRequest());

        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequisitosSeguridads() throws Exception {
        // Initialize the database
        requisitosSeguridadRepository.saveAndFlush(requisitosSeguridad);

        // Get all the requisitosSeguridadList
        restRequisitosSeguridadMockMvc.perform(get("/api/requisitos-seguridads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requisitosSeguridad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getRequisitosSeguridad() throws Exception {
        // Initialize the database
        requisitosSeguridadRepository.saveAndFlush(requisitosSeguridad);

        // Get the requisitosSeguridad
        restRequisitosSeguridadMockMvc.perform(get("/api/requisitos-seguridads/{id}", requisitosSeguridad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requisitosSeguridad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequisitosSeguridad() throws Exception {
        // Get the requisitosSeguridad
        restRequisitosSeguridadMockMvc.perform(get("/api/requisitos-seguridads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequisitosSeguridad() throws Exception {
        // Initialize the database
        requisitosSeguridadRepository.saveAndFlush(requisitosSeguridad);

        int databaseSizeBeforeUpdate = requisitosSeguridadRepository.findAll().size();

        // Update the requisitosSeguridad
        RequisitosSeguridad updatedRequisitosSeguridad = requisitosSeguridadRepository.findById(requisitosSeguridad.getId()).get();
        // Disconnect from session so that the updates on updatedRequisitosSeguridad are not directly saved in db
        em.detach(updatedRequisitosSeguridad);
        updatedRequisitosSeguridad
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restRequisitosSeguridadMockMvc.perform(put("/api/requisitos-seguridads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequisitosSeguridad)))
            .andExpect(status().isOk());

        // Validate the RequisitosSeguridad in the database
        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeUpdate);
        RequisitosSeguridad testRequisitosSeguridad = requisitosSeguridadList.get(requisitosSeguridadList.size() - 1);
        assertThat(testRequisitosSeguridad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRequisitosSeguridad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingRequisitosSeguridad() throws Exception {
        int databaseSizeBeforeUpdate = requisitosSeguridadRepository.findAll().size();

        // Create the RequisitosSeguridad

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequisitosSeguridadMockMvc.perform(put("/api/requisitos-seguridads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requisitosSeguridad)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitosSeguridad in the database
        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequisitosSeguridad() throws Exception {
        // Initialize the database
        requisitosSeguridadRepository.saveAndFlush(requisitosSeguridad);

        int databaseSizeBeforeDelete = requisitosSeguridadRepository.findAll().size();

        // Delete the requisitosSeguridad
        restRequisitosSeguridadMockMvc.perform(delete("/api/requisitos-seguridads/{id}", requisitosSeguridad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequisitosSeguridad> requisitosSeguridadList = requisitosSeguridadRepository.findAll();
        assertThat(requisitosSeguridadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequisitosSeguridad.class);
        RequisitosSeguridad requisitosSeguridad1 = new RequisitosSeguridad();
        requisitosSeguridad1.setId(1L);
        RequisitosSeguridad requisitosSeguridad2 = new RequisitosSeguridad();
        requisitosSeguridad2.setId(requisitosSeguridad1.getId());
        assertThat(requisitosSeguridad1).isEqualTo(requisitosSeguridad2);
        requisitosSeguridad2.setId(2L);
        assertThat(requisitosSeguridad1).isNotEqualTo(requisitosSeguridad2);
        requisitosSeguridad1.setId(null);
        assertThat(requisitosSeguridad1).isNotEqualTo(requisitosSeguridad2);
    }
}
