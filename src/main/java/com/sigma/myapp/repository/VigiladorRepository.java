package com.sigma.myapp.repository;

import com.sigma.myapp.domain.Vigilador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vigilador entity.
 */
@Repository
public interface VigiladorRepository extends JpaRepository<Vigilador, Long>, JpaSpecificationExecutor<Vigilador> {
    default Optional<Vigilador> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vigilador> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vigilador> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vigilador from Vigilador vigilador left join fetch vigilador.categoria left join fetch vigilador.objetivo left join fetch vigilador.user",
        countQuery = "select count(distinct vigilador) from Vigilador vigilador"
    )
    Page<Vigilador> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct vigilador from Vigilador vigilador left join fetch vigilador.categoria left join fetch vigilador.objetivo left join fetch vigilador.user"
    )
    List<Vigilador> findAllWithToOneRelationships();

    @Query(
        "select vigilador from Vigilador vigilador left join fetch vigilador.categoria left join fetch vigilador.objetivo left join fetch vigilador.user where vigilador.id =:id"
    )
    Optional<Vigilador> findOneWithToOneRelationships(@Param("id") Long id);
}
