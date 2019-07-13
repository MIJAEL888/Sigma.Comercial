package com.sigma.web.rest;

import com.sigma.domain.Sede;
import com.sigma.repository.SedeRepository;
import com.sigma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.Sede}.
 */
@RestController
@RequestMapping("/api")
public class SedeResource {

    private final Logger log = LoggerFactory.getLogger(SedeResource.class);

    private static final String ENTITY_NAME = "comercialSede";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SedeRepository sedeRepository;

    public SedeResource(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    /**
     * {@code POST  /sedes} : Create a new sede.
     *
     * @param sede the sede to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sede, or with status {@code 400 (Bad Request)} if the sede has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sedes")
    public ResponseEntity<Sede> createSede(@Valid @RequestBody Sede sede) throws URISyntaxException {
        log.debug("REST request to save Sede : {}", sede);
        if (sede.getId() != null) {
            throw new BadRequestAlertException("A new sede cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sede result = sedeRepository.save(sede);
        return ResponseEntity.created(new URI("/api/sedes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sedes} : Updates an existing sede.
     *
     * @param sede the sede to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sede,
     * or with status {@code 400 (Bad Request)} if the sede is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sede couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sedes")
    public ResponseEntity<Sede> updateSede(@Valid @RequestBody Sede sede) throws URISyntaxException {
        log.debug("REST request to update Sede : {}", sede);
        if (sede.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sede result = sedeRepository.save(sede);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sede.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sedes} : get all the sedes.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sedes in body.
     */
    @GetMapping("/sedes")
    public ResponseEntity<List<Sede>> getAllSedes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Sedes");
        Page<Sede> page = sedeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sedes/:id} : get the "id" sede.
     *
     * @param id the id of the sede to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sede, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sedes/{id}")
    public ResponseEntity<Sede> getSede(@PathVariable Long id) {
        log.debug("REST request to get Sede : {}", id);
        Optional<Sede> sede = sedeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sede);
    }

    /**
     * {@code DELETE  /sedes/:id} : delete the "id" sede.
     *
     * @param id the id of the sede to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sedes/{id}")
    public ResponseEntity<Void> deleteSede(@PathVariable Long id) {
        log.debug("REST request to delete Sede : {}", id);
        sedeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
