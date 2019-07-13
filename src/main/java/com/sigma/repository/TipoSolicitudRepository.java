package com.sigma.repository;

import com.sigma.domain.TipoSolicitud;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoSolicitud entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoSolicitudRepository extends JpaRepository<TipoSolicitud, Long> {

}
