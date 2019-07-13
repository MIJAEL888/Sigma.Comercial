package com.sigma.repository;

import com.sigma.domain.TipoInduccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoInduccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoInduccionRepository extends JpaRepository<TipoInduccion, Long> {

}
