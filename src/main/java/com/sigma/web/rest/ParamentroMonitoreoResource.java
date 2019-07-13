package com.sigma.web.rest;

import com.sigma.domain.ParamentroMonitoreo;
import com.sigma.repository.ParamentroMonitoreoRepository;
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
 * REST controller for managing {@link com.sigma.domain.ParamentroMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class ParamentroMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(ParamentroMonitoreoResource.class);

    private static final String ENTITY_NAME = "comercialParamentroMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParamentroMonitoreoRepository paramentroMonitoreoRepository;

    public ParamentroMonitoreoResource(ParamentroMonitoreoRepository paramentroMonitoreoRepository) {
        this.paramentroMonitoreoRepository = paramentroMonitoreoRepository;
    }

    /**
     * {@code POST  /paramentro-monitoreos} : Create a new paramentroMonitoreo.
     *
     * @param paramentroMonitoreo the paramentroMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paramentroMonitoreo, or with status {@code 400 (Bad Request)} if the paramentroMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paramentro-monitoreos")
    public ResponseEntity<ParamentroMonitoreo> createParamentroMonitoreo(@Valid @RequestBody ParamentroMonitoreo paramentroMonitoreo) throws URISyntaxException {
        log.debug("REST request to save ParamentroMonitoreo : {}", paramentroMonitoreo);
        if (paramentroMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new paramentroMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParamentroMonitoreo result = paramentroMonitoreoRepository.save(paramentroMonitoreo);
        return ResponseEntity.created(new URI("/api/paramentro-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paramentro-monitoreos} : Updates an existing paramentroMonitoreo.
     *
     * @param paramentroMonitoreo the paramentroMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paramentroMonitoreo,
     * or with status {@code 400 (Bad Request)} if the paramentroMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paramentroMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paramentro-monitoreos")
    public ResponseEntity<ParamentroMonitoreo> updateParamentroMonitoreo(@Valid @RequestBody ParamentroMonitoreo paramentroMonitoreo) throws URISyntaxException {
        log.debug("REST request to update ParamentroMonitoreo : {}", paramentroMonitoreo);
        if (paramentroMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParamentroMonitoreo result = paramentroMonitoreoRepository.save(paramentroMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paramentroMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paramentro-monitoreos} : get all the paramentroMonitoreos.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paramentroMonitoreos in body.
     */
    @GetMapping("/paramentro-monitoreos")
    public ResponseEntity<List<ParamentroMonitoreo>> getAllParamentroMonitoreos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ParamentroMonitoreos");
        Page<ParamentroMonitoreo> page = paramentroMonitoreoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paramentro-monitoreos/:id} : get the "id" paramentroMonitoreo.
     *
     * @param id the id of the paramentroMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paramentroMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paramentro-monitoreos/{id}")
    public ResponseEntity<ParamentroMonitoreo> getParamentroMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get ParamentroMonitoreo : {}", id);
        Optional<ParamentroMonitoreo> paramentroMonitoreo = paramentroMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paramentroMonitoreo);
    }

    /**
     * {@code DELETE  /paramentro-monitoreos/:id} : delete the "id" paramentroMonitoreo.
     *
     * @param id the id of the paramentroMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paramentro-monitoreos/{id}")
    public ResponseEntity<Void> deleteParamentroMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete ParamentroMonitoreo : {}", id);
        paramentroMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
