package com.sigma.repository;

import com.sigma.domain.Servicio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Servicio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

}
