package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.Sede;
import com.sigma.repository.SedeRepository;
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
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SedeResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class SedeResourceIT {

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUD = "AAAAAAAAAA";
    private static final String UPDATED_LATITUD = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUD = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUD = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String DEFAULT_RUTA_DOC_ESTUDIO = "AAAAAAAAAA";
    private static final String UPDATED_RUTA_DOC_ESTUDIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_DOC_ESTUDIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DOC_ESTUDIO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENTO_ESTUDIO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENTO_ESTUDIO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENTO_ESTUDIO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENTO_ESTUDIO_CONTENT_TYPE = "image/png";

    @Autowired
    private SedeRepository sedeRepository;

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

    private MockMvc restSedeMockMvc;

    private Sede sede;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SedeResource sedeResource = new SedeResource(sedeRepository);
        this.restSedeMockMvc = MockMvcBuilders.standaloneSetup(sedeResource)
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
    public static Sede createEntity(EntityManager em) {
        Sede sede = new Sede()
            .direccion(DEFAULT_DIRECCION)
            .referencia(DEFAULT_REFERENCIA)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD)
            .actividad(DEFAULT_ACTIVIDAD)
            .telefono(DEFAULT_TELEFONO)
            .descripcion(DEFAULT_DESCRIPCION)
            .comentario(DEFAULT_COMENTARIO)
            .rutaDocEstudio(DEFAULT_RUTA_DOC_ESTUDIO)
            .nombreDocEstudio(DEFAULT_NOMBRE_DOC_ESTUDIO)
            .documentoEstudio(DEFAULT_DOCUMENTO_ESTUDIO)
            .documentoEstudioContentType(DEFAULT_DOCUMENTO_ESTUDIO_CONTENT_TYPE);
        return sede;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sede createUpdatedEntity(EntityManager em) {
        Sede sede = new Sede()
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .actividad(UPDATED_ACTIVIDAD)
            .telefono(UPDATED_TELEFONO)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .rutaDocEstudio(UPDATED_RUTA_DOC_ESTUDIO)
            .nombreDocEstudio(UPDATED_NOMBRE_DOC_ESTUDIO)
            .documentoEstudio(UPDATED_DOCUMENTO_ESTUDIO)
            .documentoEstudioContentType(UPDATED_DOCUMENTO_ESTUDIO_CONTENT_TYPE);
        return sede;
    }

    @BeforeEach
    public void initTest() {
        sede = createEntity(em);
    }

    @Test
    @Transactional
    public void createSede() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().size();

        // Create the Sede
        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isCreated());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate + 1);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSede.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testSede.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testSede.getActividad()).isEqualTo(DEFAULT_ACTIVIDAD);
        assertThat(testSede.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testSede.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testSede.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testSede.getRutaDocEstudio()).isEqualTo(DEFAULT_RUTA_DOC_ESTUDIO);
        assertThat(testSede.getNombreDocEstudio()).isEqualTo(DEFAULT_NOMBRE_DOC_ESTUDIO);
        assertThat(testSede.getDocumentoEstudio()).isEqualTo(DEFAULT_DOCUMENTO_ESTUDIO);
        assertThat(testSede.getDocumentoEstudioContentType()).isEqualTo(DEFAULT_DOCUMENTO_ESTUDIO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSedeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().size();

        // Create the Sede with an existing ID
        sede.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().size();
        // set the field null
        sede.setDireccion(null);

        // Create the Sede, which fails.

        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSedes() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get all the sedeList
        restSedeMockMvc.perform(get("/api/sedes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sede.getId().intValue())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())))
            .andExpect(jsonPath("$.[*].actividad").value(hasItem(DEFAULT_ACTIVIDAD.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].rutaDocEstudio").value(hasItem(DEFAULT_RUTA_DOC_ESTUDIO.toString())))
            .andExpect(jsonPath("$.[*].nombreDocEstudio").value(hasItem(DEFAULT_NOMBRE_DOC_ESTUDIO.toString())))
            .andExpect(jsonPath("$.[*].documentoEstudioContentType").value(hasItem(DEFAULT_DOCUMENTO_ESTUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentoEstudio").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENTO_ESTUDIO))));
    }
    
    @Test
    @Transactional
    public void getSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get the sede
        restSedeMockMvc.perform(get("/api/sedes/{id}", sede.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sede.getId().intValue()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.toString()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.toString()))
            .andExpect(jsonPath("$.actividad").value(DEFAULT_ACTIVIDAD.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.rutaDocEstudio").value(DEFAULT_RUTA_DOC_ESTUDIO.toString()))
            .andExpect(jsonPath("$.nombreDocEstudio").value(DEFAULT_NOMBRE_DOC_ESTUDIO.toString()))
            .andExpect(jsonPath("$.documentoEstudioContentType").value(DEFAULT_DOCUMENTO_ESTUDIO_CONTENT_TYPE))
            .andExpect(jsonPath("$.documentoEstudio").value(Base64Utils.encodeToString(DEFAULT_DOCUMENTO_ESTUDIO)));
    }

    @Test
    @Transactional
    public void getNonExistingSede() throws Exception {
        // Get the sede
        restSedeMockMvc.perform(get("/api/sedes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Update the sede
        Sede updatedSede = sedeRepository.findById(sede.getId()).get();
        // Disconnect from session so that the updates on updatedSede are not directly saved in db
        em.detach(updatedSede);
        updatedSede
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .actividad(UPDATED_ACTIVIDAD)
            .telefono(UPDATED_TELEFONO)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .rutaDocEstudio(UPDATED_RUTA_DOC_ESTUDIO)
            .nombreDocEstudio(UPDATED_NOMBRE_DOC_ESTUDIO)
            .documentoEstudio(UPDATED_DOCUMENTO_ESTUDIO)
            .documentoEstudioContentType(UPDATED_DOCUMENTO_ESTUDIO_CONTENT_TYPE);

        restSedeMockMvc.perform(put("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSede)))
            .andExpect(status().isOk());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSede.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testSede.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testSede.getActividad()).isEqualTo(UPDATED_ACTIVIDAD);
        assertThat(testSede.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testSede.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSede.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testSede.getRutaDocEstudio()).isEqualTo(UPDATED_RUTA_DOC_ESTUDIO);
        assertThat(testSede.getNombreDocEstudio()).isEqualTo(UPDATED_NOMBRE_DOC_ESTUDIO);
        assertThat(testSede.getDocumentoEstudio()).isEqualTo(UPDATED_DOCUMENTO_ESTUDIO);
        assertThat(testSede.getDocumentoEstudioContentType()).isEqualTo(UPDATED_DOCUMENTO_ESTUDIO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Create the Sede

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSedeMockMvc.perform(put("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeDelete = sedeRepository.findAll().size();

        // Delete the sede
        restSedeMockMvc.perform(delete("/api/sedes/{id}", sede.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sede.class);
        Sede sede1 = new Sede();
        sede1.setId(1L);
        Sede sede2 = new Sede();
        sede2.setId(sede1.getId());
        assertThat(sede1).isEqualTo(sede2);
        sede2.setId(2L);
        assertThat(sede1).isNotEqualTo(sede2);
        sede1.setId(null);
        assertThat(sede1).isNotEqualTo(sede2);
    }
}
