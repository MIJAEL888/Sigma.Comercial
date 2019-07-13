package com.sigma.repository;

import com.sigma.domain.Sede;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Sede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {

}
