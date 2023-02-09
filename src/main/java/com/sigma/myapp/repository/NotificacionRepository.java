package com.sigma.myapp.repository;

import com.sigma.myapp.domain.Notificacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notificacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long>, JpaSpecificationExecutor<Notificacion> {}
