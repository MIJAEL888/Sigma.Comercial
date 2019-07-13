package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.ComponenteMonitoreo;
import com.sigma.repository.ComponenteMonitoreoRepository;
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
 * Integration tests for the {@Link ComponenteMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class ComponenteMonitoreoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private ComponenteMonitoreoRepository componenteMonitoreoRepository;

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

    private MockMvc restComponenteMonitoreoMockMvc;

    private ComponenteMonitoreo componenteMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComponenteMonitoreoResource componenteMonitoreoResource = new ComponenteMonitoreoResource(componenteMonitoreoRepository);
        this.restComponenteMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(componenteMonitoreoResource)
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
    public static ComponenteMonitoreo createEntity(EntityManager em) {
        ComponenteMonitoreo componenteMonitoreo = new ComponenteMonitoreo()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return componenteMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponenteMonitoreo createUpdatedEntity(EntityManager em) {
        ComponenteMonitoreo componenteMonitoreo = new ComponenteMonitoreo()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return componenteMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        componenteMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createComponenteMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = componenteMonitoreoRepository.findAll().size();

        // Create the ComponenteMonitoreo
        restComponenteMonitoreoMockMvc.perform(post("/api/componente-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componenteMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the ComponenteMonitoreo in the database
        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        ComponenteMonitoreo testComponenteMonitoreo = componenteMonitoreoList.get(componenteMonitoreoList.size() - 1);
        assertThat(testComponenteMonitoreo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComponenteMonitoreo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createComponenteMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = componenteMonitoreoRepository.findAll().size();

        // Create the ComponenteMonitoreo with an existing ID
        componenteMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponenteMonitoreoMockMvc.perform(post("/api/componente-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componenteMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the ComponenteMonitoreo in the database
        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = componenteMonitoreoRepository.findAll().size();
        // set the field null
        componenteMonitoreo.setNombre(null);

        // Create the ComponenteMonitoreo, which fails.

        restComponenteMonitoreoMockMvc.perform(post("/api/componente-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componenteMonitoreo)))
            .andExpect(status().isBadRequest());

        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComponenteMonitoreos() throws Exception {
        // Initialize the database
        componenteMonitoreoRepository.saveAndFlush(componenteMonitoreo);

        // Get all the componenteMonitoreoList
        restComponenteMonitoreoMockMvc.perform(get("/api/componente-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componenteMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getComponenteMonitoreo() throws Exception {
        // Initialize the database
        componenteMonitoreoRepository.saveAndFlush(componenteMonitoreo);

        // Get the componenteMonitoreo
        restComponenteMonitoreoMockMvc.perform(get("/api/componente-monitoreos/{id}", componenteMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(componenteMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComponenteMonitoreo() throws Exception {
        // Get the componenteMonitoreo
        restComponenteMonitoreoMockMvc.perform(get("/api/componente-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComponenteMonitoreo() throws Exception {
        // Initialize the database
        componenteMonitoreoRepository.saveAndFlush(componenteMonitoreo);

        int databaseSizeBeforeUpdate = componenteMonitoreoRepository.findAll().size();

        // Update the componenteMonitoreo
        ComponenteMonitoreo updatedComponenteMonitoreo = componenteMonitoreoRepository.findById(componenteMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedComponenteMonitoreo are not directly saved in db
        em.detach(updatedComponenteMonitoreo);
        updatedComponenteMonitoreo
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restComponenteMonitoreoMockMvc.perform(put("/api/componente-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedComponenteMonitoreo)))
            .andExpect(status().isOk());

        // Validate the ComponenteMonitoreo in the database
        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        ComponenteMonitoreo testComponenteMonitoreo = componenteMonitoreoList.get(componenteMonitoreoList.size() - 1);
        assertThat(testComponenteMonitoreo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComponenteMonitoreo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingComponenteMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = componenteMonitoreoRepository.findAll().size();

        // Create the ComponenteMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponenteMonitoreoMockMvc.perform(put("/api/componente-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(componenteMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the ComponenteMonitoreo in the database
        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComponenteMonitoreo() throws Exception {
        // Initialize the database
        componenteMonitoreoRepository.saveAndFlush(componenteMonitoreo);

        int databaseSizeBeforeDelete = componenteMonitoreoRepository.findAll().size();

        // Delete the componenteMonitoreo
        restComponenteMonitoreoMockMvc.perform(delete("/api/componente-monitoreos/{id}", componenteMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComponenteMonitoreo> componenteMonitoreoList = componenteMonitoreoRepository.findAll();
        assertThat(componenteMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponenteMonitoreo.class);
        ComponenteMonitoreo componenteMonitoreo1 = new ComponenteMonitoreo();
        componenteMonitoreo1.setId(1L);
        ComponenteMonitoreo componenteMonitoreo2 = new ComponenteMonitoreo();
        componenteMonitoreo2.setId(componenteMonitoreo1.getId());
        assertThat(componenteMonitoreo1).isEqualTo(componenteMonitoreo2);
        componenteMonitoreo2.setId(2L);
        assertThat(componenteMonitoreo1).isNotEqualTo(componenteMonitoreo2);
        componenteMonitoreo1.setId(null);
        assertThat(componenteMonitoreo1).isNotEqualTo(componenteMonitoreo2);
    }
}
