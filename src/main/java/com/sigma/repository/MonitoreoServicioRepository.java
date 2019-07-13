package com.sigma.repository;

import com.sigma.domain.MonitoreoServicio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MonitoreoServicio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitoreoServicioRepository extends JpaRepository<MonitoreoServicio, Long> {

}
