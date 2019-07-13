package com.sigma.repository;

import com.sigma.domain.ParamentroMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ParamentroMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamentroMonitoreoRepository extends JpaRepository<ParamentroMonitoreo, Long> {

}
