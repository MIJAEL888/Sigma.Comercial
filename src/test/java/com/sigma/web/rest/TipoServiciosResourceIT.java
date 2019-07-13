package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.TipoServicios;
import com.sigma.repository.TipoServiciosRepository;
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
 * Integration tests for the {@Link TipoServiciosResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class TipoServiciosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoServiciosRepository tipoServiciosRepository;

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

    private MockMvc restTipoServiciosMockMvc;

    private TipoServicios tipoServicios;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoServiciosResource tipoServiciosResource = new TipoServiciosResource(tipoServiciosRepository);
        this.restTipoServiciosMockMvc = MockMvcBuilders.standaloneSetup(tipoServiciosResource)
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
    public static TipoServicios createEntity(EntityManager em) {
        TipoServicios tipoServicios = new TipoServicios()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoServicios;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoServicios createUpdatedEntity(EntityManager em) {
        TipoServicios tipoServicios = new TipoServicios()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return tipoServicios;
    }

    @BeforeEach
    public void initTest() {
        tipoServicios = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoServicios() throws Exception {
        int databaseSizeBeforeCreate = tipoServiciosRepository.findAll().size();

        // Create the TipoServicios
        restTipoServiciosMockMvc.perform(post("/api/tipo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoServicios)))
            .andExpect(status().isCreated());

        // Validate the TipoServicios in the database
        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeCreate + 1);
        TipoServicios testTipoServicios = tipoServiciosList.get(tipoServiciosList.size() - 1);
        assertThat(testTipoServicios.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoServicios.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createTipoServiciosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoServiciosRepository.findAll().size();

        // Create the TipoServicios with an existing ID
        tipoServicios.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoServiciosMockMvc.perform(post("/api/tipo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoServicios)))
            .andExpect(status().isBadRequest());

        // Validate the TipoServicios in the database
        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoServiciosRepository.findAll().size();
        // set the field null
        tipoServicios.setNombre(null);

        // Create the TipoServicios, which fails.

        restTipoServiciosMockMvc.perform(post("/api/tipo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoServicios)))
            .andExpect(status().isBadRequest());

        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoServicios() throws Exception {
        // Initialize the database
        tipoServiciosRepository.saveAndFlush(tipoServicios);

        // Get all the tipoServiciosList
        restTipoServiciosMockMvc.perform(get("/api/tipo-servicios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoServicios.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getTipoServicios() throws Exception {
        // Initialize the database
        tipoServiciosRepository.saveAndFlush(tipoServicios);

        // Get the tipoServicios
        restTipoServiciosMockMvc.perform(get("/api/tipo-servicios/{id}", tipoServicios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoServicios.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoServicios() throws Exception {
        // Get the tipoServicios
        restTipoServiciosMockMvc.perform(get("/api/tipo-servicios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoServicios() throws Exception {
        // Initialize the database
        tipoServiciosRepository.saveAndFlush(tipoServicios);

        int databaseSizeBeforeUpdate = tipoServiciosRepository.findAll().size();

        // Update the tipoServicios
        TipoServicios updatedTipoServicios = tipoServiciosRepository.findById(tipoServicios.getId()).get();
        // Disconnect from session so that the updates on updatedTipoServicios are not directly saved in db
        em.detach(updatedTipoServicios);
        updatedTipoServicios
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restTipoServiciosMockMvc.perform(put("/api/tipo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoServicios)))
            .andExpect(status().isOk());

        // Validate the TipoServicios in the database
        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeUpdate);
        TipoServicios testTipoServicios = tipoServiciosList.get(tipoServiciosList.size() - 1);
        assertThat(testTipoServicios.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoServicios.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoServicios() throws Exception {
        int databaseSizeBeforeUpdate = tipoServiciosRepository.findAll().size();

        // Create the TipoServicios

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoServiciosMockMvc.perform(put("/api/tipo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoServicios)))
            .andExpect(status().isBadRequest());

        // Validate the TipoServicios in the database
        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoServicios() throws Exception {
        // Initialize the database
        tipoServiciosRepository.saveAndFlush(tipoServicios);

        int databaseSizeBeforeDelete = tipoServiciosRepository.findAll().size();

        // Delete the tipoServicios
        restTipoServiciosMockMvc.perform(delete("/api/tipo-servicios/{id}", tipoServicios.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoServicios> tipoServiciosList = tipoServiciosRepository.findAll();
        assertThat(tipoServiciosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoServicios.class);
        TipoServicios tipoServicios1 = new TipoServicios();
        tipoServicios1.setId(1L);
        TipoServicios tipoServicios2 = new TipoServicios();
        tipoServicios2.setId(tipoServicios1.getId());
        assertThat(tipoServicios1).isEqualTo(tipoServicios2);
        tipoServicios2.setId(2L);
        assertThat(tipoServicios1).isNotEqualTo(tipoServicios2);
        tipoServicios1.setId(null);
        assertThat(tipoServicios1).isNotEqualTo(tipoServicios2);
    }
}
