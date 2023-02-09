package com.sigma.myapp.repository;

import com.sigma.myapp.domain.Objetivo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Objetivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo, Long>, JpaSpecificationExecutor<Objetivo> {}
