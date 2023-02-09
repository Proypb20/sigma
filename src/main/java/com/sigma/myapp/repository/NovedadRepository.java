package com.sigma.myapp.repository;

import com.sigma.myapp.domain.Novedad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Novedad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Long>, JpaSpecificationExecutor<Novedad> {}
