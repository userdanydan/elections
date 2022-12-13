package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoLocalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GeoLocaliteRepository extends JpaRepository<GeoLocalite, String> {

    boolean existsByPkIgnoreCase(String pk);

    @Query(value="SELECT * FROM GEO_LOCALITE WHERE FK_CANTON=?1", nativeQuery = true)
    List<GeoLocalite> findByFkCanton(final Integer fkCanton);

}
