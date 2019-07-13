package com.sigma.repository;

import com.sigma.domain.ComponenteMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ComponenteMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComponenteMonitoreoRepository extends JpaRepository<ComponenteMonitoreo, Long> {

}
