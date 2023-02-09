package com.sigma.myapp.repository;

import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.domain.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Servicio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>, JpaSpecificationExecutor<Servicio> {
    List<Servicio> findAllByObjetivoAndEndDateIsNUll(Objetivo objetivo);
}
