package com.sigma.web.rest;

import com.sigma.domain.RequisitosSeguridad;
import com.sigma.repository.RequisitosSeguridadRepository;
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
 * REST controller for managing {@link com.sigma.domain.RequisitosSeguridad}.
 */
@RestController
@RequestMapping("/api")
public class RequisitosSeguridadResource {

    private final Logger log = LoggerFactory.getLogger(RequisitosSeguridadResource.class);

    private static final String ENTITY_NAME = "comercialRequisitosSeguridad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequisitosSeguridadRepository requisitosSeguridadRepository;

    public RequisitosSeguridadResource(RequisitosSeguridadRepository requisitosSeguridadRepository) {
        this.requisitosSeguridadRepository = requisitosSeguridadRepository;
    }

    /**
     * {@code POST  /requisitos-seguridads} : Create a new requisitosSeguridad.
     *
     * @param requisitosSeguridad the requisitosSeguridad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requisitosSeguridad, or with status {@code 400 (Bad Request)} if the requisitosSeguridad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requisitos-seguridads")
    public ResponseEntity<RequisitosSeguridad> createRequisitosSeguridad(@Valid @RequestBody RequisitosSeguridad requisitosSeguridad) throws URISyntaxException {
        log.debug("REST request to save RequisitosSeguridad : {}", requisitosSeguridad);
        if (requisitosSeguridad.getId() != null) {
            throw new BadRequestAlertException("A new requisitosSeguridad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequisitosSeguridad result = requisitosSeguridadRepository.save(requisitosSeguridad);
        return ResponseEntity.created(new URI("/api/requisitos-seguridads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requisitos-seguridads} : Updates an existing requisitosSeguridad.
     *
     * @param requisitosSeguridad the requisitosSeguridad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requisitosSeguridad,
     * or with status {@code 400 (Bad Request)} if the requisitosSeguridad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requisitosSeguridad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requisitos-seguridads")
    public ResponseEntity<RequisitosSeguridad> updateRequisitosSeguridad(@Valid @RequestBody RequisitosSeguridad requisitosSeguridad) throws URISyntaxException {
        log.debug("REST request to update RequisitosSeguridad : {}", requisitosSeguridad);
        if (requisitosSeguridad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequisitosSeguridad result = requisitosSeguridadRepository.save(requisitosSeguridad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requisitosSeguridad.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requisitos-seguridads} : get all the requisitosSeguridads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requisitosSeguridads in body.
     */
    @GetMapping("/requisitos-seguridads")
    public List<RequisitosSeguridad> getAllRequisitosSeguridads() {
        log.debug("REST request to get all RequisitosSeguridads");
        return requisitosSeguridadRepository.findAll();
    }

    /**
     * {@code GET  /requisitos-seguridads/:id} : get the "id" requisitosSeguridad.
     *
     * @param id the id of the requisitosSeguridad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requisitosSeguridad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requisitos-seguridads/{id}")
    public ResponseEntity<RequisitosSeguridad> getRequisitosSeguridad(@PathVariable Long id) {
        log.debug("REST request to get RequisitosSeguridad : {}", id);
        Optional<RequisitosSeguridad> requisitosSeguridad = requisitosSeguridadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requisitosSeguridad);
    }

    /**
     * {@code DELETE  /requisitos-seguridads/:id} : delete the "id" requisitosSeguridad.
     *
     * @param id the id of the requisitosSeguridad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requisitos-seguridads/{id}")
    public ResponseEntity<Void> deleteRequisitosSeguridad(@PathVariable Long id) {
        log.debug("REST request to delete RequisitosSeguridad : {}", id);
        requisitosSeguridadRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
