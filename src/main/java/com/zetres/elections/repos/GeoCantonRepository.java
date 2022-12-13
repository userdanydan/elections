package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoCanton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GeoCantonRepository extends JpaRepository<GeoCanton, Integer> {
    @Query("select g from GeoCanton g where g.fkCommune.pk = ?1")
    List<GeoCanton> findByFkCommune(Integer pk);
}
