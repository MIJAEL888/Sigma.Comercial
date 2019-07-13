package com.sigma.repository;

import com.sigma.domain.ContactoSede;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContactoSede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactoSedeRepository extends JpaRepository<ContactoSede, Long> {

}
