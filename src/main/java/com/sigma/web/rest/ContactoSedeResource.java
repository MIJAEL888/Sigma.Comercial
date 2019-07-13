package com.sigma.web.rest;

import com.sigma.domain.ContactoSede;
import com.sigma.repository.ContactoSedeRepository;
import com.sigma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.ContactoSede}.
 */
@RestController
@RequestMapping("/api")
public class ContactoSedeResource {

    private final Logger log = LoggerFactory.getLogger(ContactoSedeResource.class);

    private static final String ENTITY_NAME = "comercialContactoSede";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactoSedeRepository contactoSedeRepository;

    public ContactoSedeResource(ContactoSedeRepository contactoSedeRepository) {
        this.contactoSedeRepository = contactoSedeRepository;
    }

    /**
     * {@code POST  /contacto-sedes} : Create a new contactoSede.
     *
     * @param contactoSede the contactoSede to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactoSede, or with status {@code 400 (Bad Request)} if the contactoSede has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacto-sedes")
    public ResponseEntity<ContactoSede> createContactoSede(@Valid @RequestBody ContactoSede contactoSede) throws URISyntaxException {
        log.debug("REST request to save ContactoSede : {}", contactoSede);
        if (contactoSede.getId() != null) {
            throw new BadRequestAlertException("A new contactoSede cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactoSede result = contactoSedeRepository.save(contactoSede);
        return ResponseEntity.created(new URI("/api/contacto-sedes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacto-sedes} : Updates an existing contactoSede.
     *
     * @param contactoSede the contactoSede to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactoSede,
     * or with status {@code 400 (Bad Request)} if the contactoSede is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactoSede couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contacto-sedes")
    public ResponseEntity<ContactoSede> updateContactoSede(@Valid @RequestBody ContactoSede contactoSede) throws URISyntaxException {
        log.debug("REST request to update ContactoSede : {}", contactoSede);
        if (contactoSede.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactoSede result = contactoSedeRepository.save(contactoSede);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactoSede.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contacto-sedes} : get all the contactoSedes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactoSedes in body.
     */
    @GetMapping("/contacto-sedes")
    public List<ContactoSede> getAllContactoSedes() {
        log.debug("REST request to get all ContactoSedes");
        return contactoSedeRepository.findAll();
    }

    /**
     * {@code GET  /contacto-sedes/:id} : get the "id" contactoSede.
     *
     * @param id the id of the contactoSede to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactoSede, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contacto-sedes/{id}")
    public ResponseEntity<ContactoSede> getContactoSede(@PathVariable Long id) {
        log.debug("REST request to get ContactoSede : {}", id);
        Optional<ContactoSede> contactoSede = contactoSedeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contactoSede);
    }

    /**
     * {@code DELETE  /contacto-sedes/:id} : delete the "id" contactoSede.
     *
     * @param id the id of the contactoSede to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacto-sedes/{id}")
    public ResponseEntity<Void> deleteContactoSede(@PathVariable Long id) {
        log.debug("REST request to delete ContactoSede : {}", id);
        contactoSedeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
