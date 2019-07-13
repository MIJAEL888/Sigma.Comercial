package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.Provincia;
import com.sigma.repository.ProvinciaRepository;
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
 * Integration tests for the {@Link ProvinciaResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class ProvinciaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_UBIGEO = "AAAAAAAAAA";
    private static final String UPDATED_UBIGEO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private ProvinciaRepository provinciaRepository;

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

    private MockMvc restProvinciaMockMvc;

    private Provincia provincia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProvinciaResource provinciaResource = new ProvinciaResource(provinciaRepository);
        this.restProvinciaMockMvc = MockMvcBuilders.standaloneSetup(provinciaResource)
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
    public static Provincia createEntity(EntityManager em) {
        Provincia provincia = new Provincia()
            .nombre(DEFAULT_NOMBRE)
            .ubigeo(DEFAULT_UBIGEO)
            .descripcion(DEFAULT_DESCRIPCION);
        return provincia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provincia createUpdatedEntity(EntityManager em) {
        Provincia provincia = new Provincia()
            .nombre(UPDATED_NOMBRE)
            .ubigeo(UPDATED_UBIGEO)
            .descripcion(UPDATED_DESCRIPCION);
        return provincia;
    }

    @BeforeEach
    public void initTest() {
        provincia = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvincia() throws Exception {
        int databaseSizeBeforeCreate = provinciaRepository.findAll().size();

        // Create the Provincia
        restProvinciaMockMvc.perform(post("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provincia)))
            .andExpect(status().isCreated());

        // Validate the Provincia in the database
        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeCreate + 1);
        Provincia testProvincia = provinciaList.get(provinciaList.size() - 1);
        assertThat(testProvincia.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProvincia.getUbigeo()).isEqualTo(DEFAULT_UBIGEO);
        assertThat(testProvincia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createProvinciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = provinciaRepository.findAll().size();

        // Create the Provincia with an existing ID
        provincia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinciaMockMvc.perform(post("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provincia)))
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinciaRepository.findAll().size();
        // set the field null
        provincia.setNombre(null);

        // Create the Provincia, which fails.

        restProvinciaMockMvc.perform(post("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provincia)))
            .andExpect(status().isBadRequest());

        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUbigeoIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinciaRepository.findAll().size();
        // set the field null
        provincia.setUbigeo(null);

        // Create the Provincia, which fails.

        restProvinciaMockMvc.perform(post("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provincia)))
            .andExpect(status().isBadRequest());

        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvincias() throws Exception {
        // Initialize the database
        provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList
        restProvinciaMockMvc.perform(get("/api/provincias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provincia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ubigeo").value(hasItem(DEFAULT_UBIGEO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getProvincia() throws Exception {
        // Initialize the database
        provinciaRepository.saveAndFlush(provincia);

        // Get the provincia
        restProvinciaMockMvc.perform(get("/api/provincias/{id}", provincia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(provincia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ubigeo").value(DEFAULT_UBIGEO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProvincia() throws Exception {
        // Get the provincia
        restProvinciaMockMvc.perform(get("/api/provincias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvincia() throws Exception {
        // Initialize the database
        provinciaRepository.saveAndFlush(provincia);

        int databaseSizeBeforeUpdate = provinciaRepository.findAll().size();

        // Update the provincia
        Provincia updatedProvincia = provinciaRepository.findById(provincia.getId()).get();
        // Disconnect from session so that the updates on updatedProvincia are not directly saved in db
        em.detach(updatedProvincia);
        updatedProvincia
            .nombre(UPDATED_NOMBRE)
            .ubigeo(UPDATED_UBIGEO)
            .descripcion(UPDATED_DESCRIPCION);

        restProvinciaMockMvc.perform(put("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProvincia)))
            .andExpect(status().isOk());

        // Validate the Provincia in the database
        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeUpdate);
        Provincia testProvincia = provinciaList.get(provinciaList.size() - 1);
        assertThat(testProvincia.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProvincia.getUbigeo()).isEqualTo(UPDATED_UBIGEO);
        assertThat(testProvincia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingProvincia() throws Exception {
        int databaseSizeBeforeUpdate = provinciaRepository.findAll().size();

        // Create the Provincia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinciaMockMvc.perform(put("/api/provincias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provincia)))
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProvincia() throws Exception {
        // Initialize the database
        provinciaRepository.saveAndFlush(provincia);

        int databaseSizeBeforeDelete = provinciaRepository.findAll().size();

        // Delete the provincia
        restProvinciaMockMvc.perform(delete("/api/provincias/{id}", provincia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Provincia> provinciaList = provinciaRepository.findAll();
        assertThat(provinciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Provincia.class);
        Provincia provincia1 = new Provincia();
        provincia1.setId(1L);
        Provincia provincia2 = new Provincia();
        provincia2.setId(provincia1.getId());
        assertThat(provincia1).isEqualTo(provincia2);
        provincia2.setId(2L);
        assertThat(provincia1).isNotEqualTo(provincia2);
        provincia1.setId(null);
        assertThat(provincia1).isNotEqualTo(provincia2);
    }
}
