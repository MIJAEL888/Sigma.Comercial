package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.Servicio;
import com.sigma.repository.ServicioRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.domain.enumeration.EstadoServicio;
/**
 * Integration tests for the {@Link ServicioResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class ServicioResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOMBRE_SOLICITANTE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_SOLICITANTE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SOLICITANTE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SOLICITANTE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final EstadoServicio DEFAULT_ESTADO = EstadoServicio.REGISTRADO;
    private static final EstadoServicio UPDATED_ESTADO = EstadoServicio.COTIZADO;

    private static final String DEFAULT_CODIGO_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_CLIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_SEDE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_SEDE = "BBBBBBBBBB";

    @Autowired
    private ServicioRepository servicioRepository;

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

    private MockMvc restServicioMockMvc;

    private Servicio servicio;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServicioResource servicioResource = new ServicioResource(servicioRepository);
        this.restServicioMockMvc = MockMvcBuilders.standaloneSetup(servicioResource)
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
    public static Servicio createEntity(EntityManager em) {
        Servicio servicio = new Servicio()
            .codigo(DEFAULT_CODIGO)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
            .nombreSolicitante(DEFAULT_NOMBRE_SOLICITANTE)
            .numeroSolicitante(DEFAULT_NUMERO_SOLICITANTE)
            .observacion(DEFAULT_OBSERVACION)
            .descripcion(DEFAULT_DESCRIPCION)
            .estado(DEFAULT_ESTADO)
            .codigoCliente(DEFAULT_CODIGO_CLIENTE)
            .codigoSede(DEFAULT_CODIGO_SEDE);
        return servicio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createUpdatedEntity(EntityManager em) {
        Servicio servicio = new Servicio()
            .codigo(UPDATED_CODIGO)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .nombreSolicitante(UPDATED_NOMBRE_SOLICITANTE)
            .numeroSolicitante(UPDATED_NUMERO_SOLICITANTE)
            .observacion(UPDATED_OBSERVACION)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .codigoCliente(UPDATED_CODIGO_CLIENTE)
            .codigoSede(UPDATED_CODIGO_SEDE);
        return servicio;
    }

    @BeforeEach
    public void initTest() {
        servicio = createEntity(em);
    }

    @Test
    @Transactional
    public void createServicio() throws Exception {
        int databaseSizeBeforeCreate = servicioRepository.findAll().size();

        // Create the Servicio
        restServicioMockMvc.perform(post("/api/servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicio)))
            .andExpect(status().isCreated());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate + 1);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testServicio.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testServicio.getNombreSolicitante()).isEqualTo(DEFAULT_NOMBRE_SOLICITANTE);
        assertThat(testServicio.getNumeroSolicitante()).isEqualTo(DEFAULT_NUMERO_SOLICITANTE);
        assertThat(testServicio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testServicio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testServicio.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testServicio.getCodigoCliente()).isEqualTo(DEFAULT_CODIGO_CLIENTE);
        assertThat(testServicio.getCodigoSede()).isEqualTo(DEFAULT_CODIGO_SEDE);
    }

    @Test
    @Transactional
    public void createServicioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servicioRepository.findAll().size();

        // Create the Servicio with an existing ID
        servicio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioMockMvc.perform(post("/api/servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicio)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllServicios() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList
        restServicioMockMvc.perform(get("/api/servicios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].nombreSolicitante").value(hasItem(DEFAULT_NOMBRE_SOLICITANTE.toString())))
            .andExpect(jsonPath("$.[*].numeroSolicitante").value(hasItem(DEFAULT_NUMERO_SOLICITANTE.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].codigoCliente").value(hasItem(DEFAULT_CODIGO_CLIENTE.toString())))
            .andExpect(jsonPath("$.[*].codigoSede").value(hasItem(DEFAULT_CODIGO_SEDE.toString())));
    }
    
    @Test
    @Transactional
    public void getServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get the servicio
        restServicioMockMvc.perform(get("/api/servicios/{id}", servicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(servicio.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.nombreSolicitante").value(DEFAULT_NOMBRE_SOLICITANTE.toString()))
            .andExpect(jsonPath("$.numeroSolicitante").value(DEFAULT_NUMERO_SOLICITANTE.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.codigoCliente").value(DEFAULT_CODIGO_CLIENTE.toString()))
            .andExpect(jsonPath("$.codigoSede").value(DEFAULT_CODIGO_SEDE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServicio() throws Exception {
        // Get the servicio
        restServicioMockMvc.perform(get("/api/servicios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio
        Servicio updatedServicio = servicioRepository.findById(servicio.getId()).get();
        // Disconnect from session so that the updates on updatedServicio are not directly saved in db
        em.detach(updatedServicio);
        updatedServicio
            .codigo(UPDATED_CODIGO)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .nombreSolicitante(UPDATED_NOMBRE_SOLICITANTE)
            .numeroSolicitante(UPDATED_NUMERO_SOLICITANTE)
            .observacion(UPDATED_OBSERVACION)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .codigoCliente(UPDATED_CODIGO_CLIENTE)
            .codigoSede(UPDATED_CODIGO_SEDE);

        restServicioMockMvc.perform(put("/api/servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServicio)))
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testServicio.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testServicio.getNombreSolicitante()).isEqualTo(UPDATED_NOMBRE_SOLICITANTE);
        assertThat(testServicio.getNumeroSolicitante()).isEqualTo(UPDATED_NUMERO_SOLICITANTE);
        assertThat(testServicio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testServicio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testServicio.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testServicio.getCodigoCliente()).isEqualTo(UPDATED_CODIGO_CLIENTE);
        assertThat(testServicio.getCodigoSede()).isEqualTo(UPDATED_CODIGO_SEDE);
    }

    @Test
    @Transactional
    public void updateNonExistingServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Create the Servicio

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc.perform(put("/api/servicios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicio)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeDelete = servicioRepository.findAll().size();

        // Delete the servicio
        restServicioMockMvc.perform(delete("/api/servicios/{id}", servicio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servicio.class);
        Servicio servicio1 = new Servicio();
        servicio1.setId(1L);
        Servicio servicio2 = new Servicio();
        servicio2.setId(servicio1.getId());
        assertThat(servicio1).isEqualTo(servicio2);
        servicio2.setId(2L);
        assertThat(servicio1).isNotEqualTo(servicio2);
        servicio1.setId(null);
        assertThat(servicio1).isNotEqualTo(servicio2);
    }
}
