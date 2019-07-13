package com.sigma.web.rest;

import com.sigma.domain.MonitoreoServicio;
import com.sigma.repository.MonitoreoServicioRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.MonitoreoServicio}.
 */
@RestController
@RequestMapping("/api")
public class MonitoreoServicioResource {

    private final Logger log = LoggerFactory.getLogger(MonitoreoServicioResource.class);

    private static final String ENTITY_NAME = "comercialMonitoreoServicio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoreoServicioRepository monitoreoServicioRepository;

    public MonitoreoServicioResource(MonitoreoServicioRepository monitoreoServicioRepository) {
        this.monitoreoServicioRepository = monitoreoServicioRepository;
    }

    /**
     * {@code POST  /monitoreo-servicios} : Create a new monitoreoServicio.
     *
     * @param monitoreoServicio the monitoreoServicio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoreoServicio, or with status {@code 400 (Bad Request)} if the monitoreoServicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monitoreo-servicios")
    public ResponseEntity<MonitoreoServicio> createMonitoreoServicio(@RequestBody MonitoreoServicio monitoreoServicio) throws URISyntaxException {
        log.debug("REST request to save MonitoreoServicio : {}", monitoreoServicio);
        if (monitoreoServicio.getId() != null) {
            throw new BadRequestAlertException("A new monitoreoServicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonitoreoServicio result = monitoreoServicioRepository.save(monitoreoServicio);
        return ResponseEntity.created(new URI("/api/monitoreo-servicios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monitoreo-servicios} : Updates an existing monitoreoServicio.
     *
     * @param monitoreoServicio the monitoreoServicio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoreoServicio,
     * or with status {@code 400 (Bad Request)} if the monitoreoServicio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoreoServicio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monitoreo-servicios")
    public ResponseEntity<MonitoreoServicio> updateMonitoreoServicio(@RequestBody MonitoreoServicio monitoreoServicio) throws URISyntaxException {
        log.debug("REST request to update MonitoreoServicio : {}", monitoreoServicio);
        if (monitoreoServicio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MonitoreoServicio result = monitoreoServicioRepository.save(monitoreoServicio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monitoreoServicio.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /monitoreo-servicios} : get all the monitoreoServicios.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoreoServicios in body.
     */
    @GetMapping("/monitoreo-servicios")
    public ResponseEntity<List<MonitoreoServicio>> getAllMonitoreoServicios(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of MonitoreoServicios");
        Page<MonitoreoServicio> page = monitoreoServicioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoreo-servicios/:id} : get the "id" monitoreoServicio.
     *
     * @param id the id of the monitoreoServicio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoreoServicio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monitoreo-servicios/{id}")
    public ResponseEntity<MonitoreoServicio> getMonitoreoServicio(@PathVariable Long id) {
        log.debug("REST request to get MonitoreoServicio : {}", id);
        Optional<MonitoreoServicio> monitoreoServicio = monitoreoServicioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(monitoreoServicio);
    }

    /**
     * {@code DELETE  /monitoreo-servicios/:id} : delete the "id" monitoreoServicio.
     *
     * @param id the id of the monitoreoServicio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monitoreo-servicios/{id}")
    public ResponseEntity<Void> deleteMonitoreoServicio(@PathVariable Long id) {
        log.debug("REST request to delete MonitoreoServicio : {}", id);
        monitoreoServicioRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
