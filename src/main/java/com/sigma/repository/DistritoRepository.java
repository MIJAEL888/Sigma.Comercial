package com.sigma.repository;

import com.sigma.domain.Distrito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Distrito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {

}
