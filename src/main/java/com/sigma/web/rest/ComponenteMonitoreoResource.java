package com.sigma.web.rest;

import com.sigma.domain.ComponenteMonitoreo;
import com.sigma.repository.ComponenteMonitoreoRepository;
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
 * REST controller for managing {@link com.sigma.domain.ComponenteMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class ComponenteMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(ComponenteMonitoreoResource.class);

    private static final String ENTITY_NAME = "comercialComponenteMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponenteMonitoreoRepository componenteMonitoreoRepository;

    public ComponenteMonitoreoResource(ComponenteMonitoreoRepository componenteMonitoreoRepository) {
        this.componenteMonitoreoRepository = componenteMonitoreoRepository;
    }

    /**
     * {@code POST  /componente-monitoreos} : Create a new componenteMonitoreo.
     *
     * @param componenteMonitoreo the componenteMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componenteMonitoreo, or with status {@code 400 (Bad Request)} if the componenteMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/componente-monitoreos")
    public ResponseEntity<ComponenteMonitoreo> createComponenteMonitoreo(@Valid @RequestBody ComponenteMonitoreo componenteMonitoreo) throws URISyntaxException {
        log.debug("REST request to save ComponenteMonitoreo : {}", componenteMonitoreo);
        if (componenteMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new componenteMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponenteMonitoreo result = componenteMonitoreoRepository.save(componenteMonitoreo);
        return ResponseEntity.created(new URI("/api/componente-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /componente-monitoreos} : Updates an existing componenteMonitoreo.
     *
     * @param componenteMonitoreo the componenteMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componenteMonitoreo,
     * or with status {@code 400 (Bad Request)} if the componenteMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componenteMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/componente-monitoreos")
    public ResponseEntity<ComponenteMonitoreo> updateComponenteMonitoreo(@Valid @RequestBody ComponenteMonitoreo componenteMonitoreo) throws URISyntaxException {
        log.debug("REST request to update ComponenteMonitoreo : {}", componenteMonitoreo);
        if (componenteMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ComponenteMonitoreo result = componenteMonitoreoRepository.save(componenteMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componenteMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /componente-monitoreos} : get all the componenteMonitoreos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componenteMonitoreos in body.
     */
    @GetMapping("/componente-monitoreos")
    public List<ComponenteMonitoreo> getAllComponenteMonitoreos() {
        log.debug("REST request to get all ComponenteMonitoreos");
        return componenteMonitoreoRepository.findAll();
    }

    /**
     * {@code GET  /componente-monitoreos/:id} : get the "id" componenteMonitoreo.
     *
     * @param id the id of the componenteMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componenteMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/componente-monitoreos/{id}")
    public ResponseEntity<ComponenteMonitoreo> getComponenteMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get ComponenteMonitoreo : {}", id);
        Optional<ComponenteMonitoreo> componenteMonitoreo = componenteMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(componenteMonitoreo);
    }

    /**
     * {@code DELETE  /componente-monitoreos/:id} : delete the "id" componenteMonitoreo.
     *
     * @param id the id of the componenteMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/componente-monitoreos/{id}")
    public ResponseEntity<Void> deleteComponenteMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete ComponenteMonitoreo : {}", id);
        componenteMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
