package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeoCentreRepository extends JpaRepository<GeoCentre, String> {

    @Query(value="SELECT * FROM GEO_CENTRE WHERE FK_LOCALITE=?1", nativeQuery = true)
    List<GeoCentre> findByFkLocalite(final String fkLocalite);

    GeoCentre[] findByNameContainingIgnoreCase(String name);
    boolean existsByPkIgnoreCase(String pk);

}
