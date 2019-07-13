package com.sigma.web.rest;

import com.sigma.domain.Distrito;
import com.sigma.repository.DistritoRepository;
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
 * REST controller for managing {@link com.sigma.domain.Distrito}.
 */
@RestController
@RequestMapping("/api")
public class DistritoResource {

    private final Logger log = LoggerFactory.getLogger(DistritoResource.class);

    private static final String ENTITY_NAME = "comercialDistrito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistritoRepository distritoRepository;

    public DistritoResource(DistritoRepository distritoRepository) {
        this.distritoRepository = distritoRepository;
    }

    /**
     * {@code POST  /distritos} : Create a new distrito.
     *
     * @param distrito the distrito to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new distrito, or with status {@code 400 (Bad Request)} if the distrito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/distritos")
    public ResponseEntity<Distrito> createDistrito(@Valid @RequestBody Distrito distrito) throws URISyntaxException {
        log.debug("REST request to save Distrito : {}", distrito);
        if (distrito.getId() != null) {
            throw new BadRequestAlertException("A new distrito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Distrito result = distritoRepository.save(distrito);
        return ResponseEntity.created(new URI("/api/distritos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /distritos} : Updates an existing distrito.
     *
     * @param distrito the distrito to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distrito,
     * or with status {@code 400 (Bad Request)} if the distrito is not valid,
     * or with status {@code 500 (Internal Server Error)} if the distrito couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/distritos")
    public ResponseEntity<Distrito> updateDistrito(@Valid @RequestBody Distrito distrito) throws URISyntaxException {
        log.debug("REST request to update Distrito : {}", distrito);
        if (distrito.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Distrito result = distritoRepository.save(distrito);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, distrito.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /distritos} : get all the distritos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of distritos in body.
     */
    @GetMapping("/distritos")
    public List<Distrito> getAllDistritos() {
        log.debug("REST request to get all Distritos");
        return distritoRepository.findAll();
    }

    /**
     * {@code GET  /distritos/:id} : get the "id" distrito.
     *
     * @param id the id of the distrito to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the distrito, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/distritos/{id}")
    public ResponseEntity<Distrito> getDistrito(@PathVariable Long id) {
        log.debug("REST request to get Distrito : {}", id);
        Optional<Distrito> distrito = distritoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(distrito);
    }

    /**
     * {@code DELETE  /distritos/:id} : delete the "id" distrito.
     *
     * @param id the id of the distrito to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/distritos/{id}")
    public ResponseEntity<Void> deleteDistrito(@PathVariable Long id) {
        log.debug("REST request to delete Distrito : {}", id);
        distritoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
