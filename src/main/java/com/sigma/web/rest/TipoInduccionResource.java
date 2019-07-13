package com.sigma.web.rest;

import com.sigma.domain.TipoInduccion;
import com.sigma.repository.TipoInduccionRepository;
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
 * REST controller for managing {@link com.sigma.domain.TipoInduccion}.
 */
@RestController
@RequestMapping("/api")
public class TipoInduccionResource {

    private final Logger log = LoggerFactory.getLogger(TipoInduccionResource.class);

    private static final String ENTITY_NAME = "comercialTipoInduccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoInduccionRepository tipoInduccionRepository;

    public TipoInduccionResource(TipoInduccionRepository tipoInduccionRepository) {
        this.tipoInduccionRepository = tipoInduccionRepository;
    }

    /**
     * {@code POST  /tipo-induccions} : Create a new tipoInduccion.
     *
     * @param tipoInduccion the tipoInduccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoInduccion, or with status {@code 400 (Bad Request)} if the tipoInduccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-induccions")
    public ResponseEntity<TipoInduccion> createTipoInduccion(@Valid @RequestBody TipoInduccion tipoInduccion) throws URISyntaxException {
        log.debug("REST request to save TipoInduccion : {}", tipoInduccion);
        if (tipoInduccion.getId() != null) {
            throw new BadRequestAlertException("A new tipoInduccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoInduccion result = tipoInduccionRepository.save(tipoInduccion);
        return ResponseEntity.created(new URI("/api/tipo-induccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-induccions} : Updates an existing tipoInduccion.
     *
     * @param tipoInduccion the tipoInduccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoInduccion,
     * or with status {@code 400 (Bad Request)} if the tipoInduccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoInduccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-induccions")
    public ResponseEntity<TipoInduccion> updateTipoInduccion(@Valid @RequestBody TipoInduccion tipoInduccion) throws URISyntaxException {
        log.debug("REST request to update TipoInduccion : {}", tipoInduccion);
        if (tipoInduccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoInduccion result = tipoInduccionRepository.save(tipoInduccion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoInduccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-induccions} : get all the tipoInduccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoInduccions in body.
     */
    @GetMapping("/tipo-induccions")
    public List<TipoInduccion> getAllTipoInduccions() {
        log.debug("REST request to get all TipoInduccions");
        return tipoInduccionRepository.findAll();
    }

    /**
     * {@code GET  /tipo-induccions/:id} : get the "id" tipoInduccion.
     *
     * @param id the id of the tipoInduccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoInduccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-induccions/{id}")
    public ResponseEntity<TipoInduccion> getTipoInduccion(@PathVariable Long id) {
        log.debug("REST request to get TipoInduccion : {}", id);
        Optional<TipoInduccion> tipoInduccion = tipoInduccionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoInduccion);
    }

    /**
     * {@code DELETE  /tipo-induccions/:id} : delete the "id" tipoInduccion.
     *
     * @param id the id of the tipoInduccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-induccions/{id}")
    public ResponseEntity<Void> deleteTipoInduccion(@PathVariable Long id) {
        log.debug("REST request to delete TipoInduccion : {}", id);
        tipoInduccionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
