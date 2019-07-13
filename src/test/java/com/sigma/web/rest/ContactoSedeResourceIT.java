package com.sigma.web.rest;

import com.sigma.ComercialApp;
import com.sigma.domain.ContactoSede;
import com.sigma.repository.ContactoSedeRepository;
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
 * Integration tests for the {@Link ContactoSedeResource} REST controller.
 */
@SpringBootTest(classes = ComercialApp.class)
public class ContactoSedeResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String DEFAULT_POSICION = "AAAAAAAAAA";
    private static final String UPDATED_POSICION = "BBBBBBBBBB";

    @Autowired
    private ContactoSedeRepository contactoSedeRepository;

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

    private MockMvc restContactoSedeMockMvc;

    private ContactoSede contactoSede;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactoSedeResource contactoSedeResource = new ContactoSedeResource(contactoSedeRepository);
        this.restContactoSedeMockMvc = MockMvcBuilders.standaloneSetup(contactoSedeResource)
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
    public static ContactoSede createEntity(EntityManager em) {
        ContactoSede contactoSede = new ContactoSede()
            .nombre(DEFAULT_NOMBRE)
            .telefono(DEFAULT_TELEFONO)
            .correo(DEFAULT_CORREO)
            .posicion(DEFAULT_POSICION);
        return contactoSede;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactoSede createUpdatedEntity(EntityManager em) {
        ContactoSede contactoSede = new ContactoSede()
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO)
            .posicion(UPDATED_POSICION);
        return contactoSede;
    }

    @BeforeEach
    public void initTest() {
        contactoSede = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactoSede() throws Exception {
        int databaseSizeBeforeCreate = contactoSedeRepository.findAll().size();

        // Create the ContactoSede
        restContactoSedeMockMvc.perform(post("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactoSede)))
            .andExpect(status().isCreated());

        // Validate the ContactoSede in the database
        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeCreate + 1);
        ContactoSede testContactoSede = contactoSedeList.get(contactoSedeList.size() - 1);
        assertThat(testContactoSede.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testContactoSede.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testContactoSede.getCorreo()).isEqualTo(DEFAULT_CORREO);
        assertThat(testContactoSede.getPosicion()).isEqualTo(DEFAULT_POSICION);
    }

    @Test
    @Transactional
    public void createContactoSedeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactoSedeRepository.findAll().size();

        // Create the ContactoSede with an existing ID
        contactoSede.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactoSedeMockMvc.perform(post("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactoSede)))
            .andExpect(status().isBadRequest());

        // Validate the ContactoSede in the database
        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactoSedeRepository.findAll().size();
        // set the field null
        contactoSede.setNombre(null);

        // Create the ContactoSede, which fails.

        restContactoSedeMockMvc.perform(post("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactoSede)))
            .andExpect(status().isBadRequest());

        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactoSedeRepository.findAll().size();
        // set the field null
        contactoSede.setTelefono(null);

        // Create the ContactoSede, which fails.

        restContactoSedeMockMvc.perform(post("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactoSede)))
            .andExpect(status().isBadRequest());

        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactoSedes() throws Exception {
        // Initialize the database
        contactoSedeRepository.saveAndFlush(contactoSede);

        // Get all the contactoSedeList
        restContactoSedeMockMvc.perform(get("/api/contacto-sedes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactoSede.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO.toString())))
            .andExpect(jsonPath("$.[*].posicion").value(hasItem(DEFAULT_POSICION.toString())));
    }
    
    @Test
    @Transactional
    public void getContactoSede() throws Exception {
        // Initialize the database
        contactoSedeRepository.saveAndFlush(contactoSede);

        // Get the contactoSede
        restContactoSedeMockMvc.perform(get("/api/contacto-sedes/{id}", contactoSede.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactoSede.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO.toString()))
            .andExpect(jsonPath("$.posicion").value(DEFAULT_POSICION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactoSede() throws Exception {
        // Get the contactoSede
        restContactoSedeMockMvc.perform(get("/api/contacto-sedes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactoSede() throws Exception {
        // Initialize the database
        contactoSedeRepository.saveAndFlush(contactoSede);

        int databaseSizeBeforeUpdate = contactoSedeRepository.findAll().size();

        // Update the contactoSede
        ContactoSede updatedContactoSede = contactoSedeRepository.findById(contactoSede.getId()).get();
        // Disconnect from session so that the updates on updatedContactoSede are not directly saved in db
        em.detach(updatedContactoSede);
        updatedContactoSede
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO)
            .posicion(UPDATED_POSICION);

        restContactoSedeMockMvc.perform(put("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactoSede)))
            .andExpect(status().isOk());

        // Validate the ContactoSede in the database
        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeUpdate);
        ContactoSede testContactoSede = contactoSedeList.get(contactoSedeList.size() - 1);
        assertThat(testContactoSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testContactoSede.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testContactoSede.getCorreo()).isEqualTo(UPDATED_CORREO);
        assertThat(testContactoSede.getPosicion()).isEqualTo(UPDATED_POSICION);
    }

    @Test
    @Transactional
    public void updateNonExistingContactoSede() throws Exception {
        int databaseSizeBeforeUpdate = contactoSedeRepository.findAll().size();

        // Create the ContactoSede

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoSedeMockMvc.perform(put("/api/contacto-sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactoSede)))
            .andExpect(status().isBadRequest());

        // Validate the ContactoSede in the database
        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactoSede() throws Exception {
        // Initialize the database
        contactoSedeRepository.saveAndFlush(contactoSede);

        int databaseSizeBeforeDelete = contactoSedeRepository.findAll().size();

        // Delete the contactoSede
        restContactoSedeMockMvc.perform(delete("/api/contacto-sedes/{id}", contactoSede.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactoSede> contactoSedeList = contactoSedeRepository.findAll();
        assertThat(contactoSedeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactoSede.class);
        ContactoSede contactoSede1 = new ContactoSede();
        contactoSede1.setId(1L);
        ContactoSede contactoSede2 = new ContactoSede();
        contactoSede2.setId(contactoSede1.getId());
        assertThat(contactoSede1).isEqualTo(contactoSede2);
        contactoSede2.setId(2L);
        assertThat(contactoSede1).isNotEqualTo(contactoSede2);
        contactoSede1.setId(null);
        assertThat(contactoSede1).isNotEqualTo(contactoSede2);
    }
}
