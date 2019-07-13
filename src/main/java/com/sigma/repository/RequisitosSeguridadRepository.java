package com.sigma.repository;

import com.sigma.domain.RequisitosSeguridad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequisitosSeguridad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitosSeguridadRepository extends JpaRepository<RequisitosSeguridad, Long> {

}
