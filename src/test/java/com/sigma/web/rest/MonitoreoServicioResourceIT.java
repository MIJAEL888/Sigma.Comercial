package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.MonitoreoServicio;
import com.sigma.repository.MonitoreoServicioRepository;
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
 * Integration tests for the {@Link MonitoreoServicioResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class MonitoreoServicioResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Float DEFAULT_COSTO_TOTAL = 1F;
    private static final Float UPDATED_COSTO_TOTAL = 2F;

    @Autowired
    private MonitoreoServicioRepository monitoreoServicioRepository;

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

    private MockMvc restMonitoreoServicioMockMvc;

    private MonitoreoServicio monitoreoServicio;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonitoreoServicioResource monitoreoServicioResource = new MonitoreoServicioResource(monitoreoServicioRepository);
        this.restMonitoreoServicioMockMvc = MockMvcBuilders.standaloneSetup(monitoreoServicioResource)
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
    public static MonitoreoServicio createEntity(EntityManager em) {
        MonitoreoServicio monitoreoServicio = new MonitoreoServicio()
            .cantidad(DEFAULT_CANTIDAD)
            .costoTotal(DEFAULT_COSTO_TOTAL);
        return monitoreoServicio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoreoServicio createUpdatedEntity(EntityManager em) {
        MonitoreoServicio monitoreoServicio = new MonitoreoServicio()
            .cantidad(UPDATED_CANTIDAD)
            .costoTotal(UPDATED_COSTO_TOTAL);
        return monitoreoServicio;
    }

    @BeforeEach
    public void initTest() {
        monitoreoServicio = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonitoreoServicio() throws Exception {
        int databaseSizeBeforeCreate = monitoreoServicioRepository.findAll().size();

        // Create the MonitoreoServicio
        restMonitoreoServicioMockMvc.perform(post("/api/monitoreo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitoreoServicio)))
            .andExpect(status().isCreated());

        // Validate the MonitoreoServicio in the database
        List<MonitoreoServicio> monitoreoServicioList = monitoreoServicioRepository.findAll();
        assertThat(monitoreoServicioList).hasSize(databaseSizeBeforeCreate + 1);
        MonitoreoServicio testMonitoreoServicio = monitoreoServicioList.get(monitoreoServicioList.size() - 1);
        assertThat(testMonitoreoServicio.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testMonitoreoServicio.getCostoTotal()).isEqualTo(DEFAULT_COSTO_TOTAL);
    }

    @Test
    @Transactional
    public void createMonitoreoServicioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monitoreoServicioRepository.findAll().size();

        // Create the MonitoreoServicio with an existing ID
        monitoreoServicio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoreoServicioMockMvc.perform(post("/api/monitoreo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitoreoServicio)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoreoServicio in the database
        List<MonitoreoServicio> monitoreoServicioList = monitoreoServicioRepository.findAll();
        assertThat(monitoreoServicioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMonitoreoServicios() throws Exception {
        // Initialize the database
        monitoreoServicioRepository.saveAndFlush(monitoreoServicio);

        // Get all the monitoreoServicioList
        restMonitoreoServicioMockMvc.perform(get("/api/monitoreo-servicios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoreoServicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].costoTotal").value(hasItem(DEFAULT_COSTO_TOTAL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getMonitoreoServicio() throws Exception {
        // Initialize the database
        monitoreoServicioRepository.saveAndFlush(monitoreoServicio);

        // Get the monitoreoServicio
        restMonitoreoServicioMockMvc.perform(get("/api/monitoreo-servicios/{id}", monitoreoServicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monitoreoServicio.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.costoTotal").value(DEFAULT_COSTO_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMonitoreoServicio() throws Exception {
        // Get the monitoreoServicio
        restMonitoreoServicioMockMvc.perform(get("/api/monitoreo-servicios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonitoreoServicio() throws Exception {
        // Initialize the database
        monitoreoServicioRepository.saveAndFlush(monitoreoServicio);

        int databaseSizeBeforeUpdate = monitoreoServicioRepository.findAll().size();

        // Update the monitoreoServicio
        MonitoreoServicio updatedMonitoreoServicio = monitoreoServicioRepository.findById(monitoreoServicio.getId()).get();
        // Disconnect from session so that the updates on updatedMonitoreoServicio are not directly saved in db
        em.detach(updatedMonitoreoServicio);
        updatedMonitoreoServicio
            .cantidad(UPDATED_CANTIDAD)
            .costoTotal(UPDATED_COSTO_TOTAL);

        restMonitoreoServicioMockMvc.perform(put("/api/monitoreo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMonitoreoServicio)))
            .andExpect(status().isOk());

        // Validate the MonitoreoServicio in the database
        List<MonitoreoServicio> monitoreoServicioList = monitoreoServicioRepository.findAll();
        assertThat(monitoreoServicioList).hasSize(databaseSizeBeforeUpdate);
        MonitoreoServicio testMonitoreoServicio = monitoreoServicioList.get(monitoreoServicioList.size() - 1);
        assertThat(testMonitoreoServicio.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testMonitoreoServicio.getCostoTotal()).isEqualTo(UPDATED_COSTO_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingMonitoreoServicio() throws Exception {
        int databaseSizeBeforeUpdate = monitoreoServicioRepository.findAll().size();

        // Create the MonitoreoServicio

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoreoServicioMockMvc.perform(put("/api/monitoreo-servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitoreoServicio)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoreoServicio in the database
        List<MonitoreoServicio> monitoreoServicioList = monitoreoServicioRepository.findAll();
        assertThat(monitoreoServicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMonitoreoServicio() throws Exception {
        // Initialize the database
        monitoreoServicioRepository.saveAndFlush(monitoreoServicio);

        int databaseSizeBeforeDelete = monitoreoServicioRepository.findAll().size();

        // Delete the monitoreoServicio
        restMonitoreoServicioMockMvc.perform(delete("/api/monitoreo-servicios/{id}", monitoreoServicio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonitoreoServicio> monitoreoServicioList = monitoreoServicioRepository.findAll();
        assertThat(monitoreoServicioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoreoServicio.class);
        MonitoreoServicio monitoreoServicio1 = new MonitoreoServicio();
        monitoreoServicio1.setId(1L);
        MonitoreoServicio monitoreoServicio2 = new MonitoreoServicio();
        monitoreoServicio2.setId(monitoreoServicio1.getId());
        assertThat(monitoreoServicio1).isEqualTo(monitoreoServicio2);
        monitoreoServicio2.setId(2L);
        assertThat(monitoreoServicio1).isNotEqualTo(monitoreoServicio2);
        monitoreoServicio1.setId(null);
        assertThat(monitoreoServicio1).isNotEqualTo(monitoreoServicio2);
    }
}
