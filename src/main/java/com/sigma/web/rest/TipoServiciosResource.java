package com.sigma.web.rest;

import com.sigma.domain.TipoServicios;
import com.sigma.repository.TipoServiciosRepository;
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
 * REST controller for managing {@link com.sigma.domain.TipoServicios}.
 */
@RestController
@RequestMapping("/api")
public class TipoServiciosResource {

    private final Logger log = LoggerFactory.getLogger(TipoServiciosResource.class);

    private static final String ENTITY_NAME = "comercialTipoServicios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoServiciosRepository tipoServiciosRepository;

    public TipoServiciosResource(TipoServiciosRepository tipoServiciosRepository) {
        this.tipoServiciosRepository = tipoServiciosRepository;
    }

    /**
     * {@code POST  /tipo-servicios} : Create a new tipoServicios.
     *
     * @param tipoServicios the tipoServicios to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoServicios, or with status {@code 400 (Bad Request)} if the tipoServicios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-servicios")
    public ResponseEntity<TipoServicios> createTipoServicios(@Valid @RequestBody TipoServicios tipoServicios) throws URISyntaxException {
        log.debug("REST request to save TipoServicios : {}", tipoServicios);
        if (tipoServicios.getId() != null) {
            throw new BadRequestAlertException("A new tipoServicios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoServicios result = tipoServiciosRepository.save(tipoServicios);
        return ResponseEntity.created(new URI("/api/tipo-servicios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-servicios} : Updates an existing tipoServicios.
     *
     * @param tipoServicios the tipoServicios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoServicios,
     * or with status {@code 400 (Bad Request)} if the tipoServicios is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoServicios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-servicios")
    public ResponseEntity<TipoServicios> updateTipoServicios(@Valid @RequestBody TipoServicios tipoServicios) throws URISyntaxException {
        log.debug("REST request to update TipoServicios : {}", tipoServicios);
        if (tipoServicios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoServicios result = tipoServiciosRepository.save(tipoServicios);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoServicios.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-servicios} : get all the tipoServicios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoServicios in body.
     */
    @GetMapping("/tipo-servicios")
    public List<TipoServicios> getAllTipoServicios() {
        log.debug("REST request to get all TipoServicios");
        return tipoServiciosRepository.findAll();
    }

    /**
     * {@code GET  /tipo-servicios/:id} : get the "id" tipoServicios.
     *
     * @param id the id of the tipoServicios to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoServicios, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-servicios/{id}")
    public ResponseEntity<TipoServicios> getTipoServicios(@PathVariable Long id) {
        log.debug("REST request to get TipoServicios : {}", id);
        Optional<TipoServicios> tipoServicios = tipoServiciosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoServicios);
    }

    /**
     * {@code DELETE  /tipo-servicios/:id} : delete the "id" tipoServicios.
     *
     * @param id the id of the tipoServicios to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-servicios/{id}")
    public ResponseEntity<Void> deleteTipoServicios(@PathVariable Long id) {
        log.debug("REST request to delete TipoServicios : {}", id);
        tipoServiciosRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
