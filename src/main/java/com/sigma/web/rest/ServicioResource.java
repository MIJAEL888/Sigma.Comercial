package com.sigma.web.rest;

import com.sigma.domain.Servicio;
import com.sigma.repository.ServicioRepository;
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
 * REST controller for managing {@link com.sigma.domain.Servicio}.
 */
@RestController
@RequestMapping("/api")
public class ServicioResource {

    private final Logger log = LoggerFactory.getLogger(ServicioResource.class);

    private static final String ENTITY_NAME = "comercialServicio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicioRepository servicioRepository;

    public ServicioResource(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    /**
     * {@code POST  /servicios} : Create a new servicio.
     *
     * @param servicio the servicio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicio, or with status {@code 400 (Bad Request)} if the servicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/servicios")
    public ResponseEntity<Servicio> createServicio(@RequestBody Servicio servicio) throws URISyntaxException {
        log.debug("REST request to save Servicio : {}", servicio);
        if (servicio.getId() != null) {
            throw new BadRequestAlertException("A new servicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Servicio result = servicioRepository.save(servicio);
        return ResponseEntity.created(new URI("/api/servicios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /servicios} : Updates an existing servicio.
     *
     * @param servicio the servicio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicio,
     * or with status {@code 400 (Bad Request)} if the servicio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/servicios")
    public ResponseEntity<Servicio> updateServicio(@RequestBody Servicio servicio) throws URISyntaxException {
        log.debug("REST request to update Servicio : {}", servicio);
        if (servicio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Servicio result = servicioRepository.save(servicio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, servicio.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /servicios} : get all the servicios.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicios in body.
     */
    @GetMapping("/servicios")
    public ResponseEntity<List<Servicio>> getAllServicios(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Servicios");
        Page<Servicio> page = servicioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /servicios/:id} : get the "id" servicio.
     *
     * @param id the id of the servicio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/servicios/{id}")
    public ResponseEntity<Servicio> getServicio(@PathVariable Long id) {
        log.debug("REST request to get Servicio : {}", id);
        Optional<Servicio> servicio = servicioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(servicio);
    }

    /**
     * {@code DELETE  /servicios/:id} : delete the "id" servicio.
     *
     * @param id the id of the servicio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        log.debug("REST request to delete Servicio : {}", id);
        servicioRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
