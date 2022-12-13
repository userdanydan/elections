package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoCommune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GeoCommuneRepository extends JpaRepository<GeoCommune, Integer> {
    @Query("""
            select g from GeoCommune g where g.fkPrefecture.pk = ?1""")
    List<GeoCommune> findByFkPrefecture(Integer pk);
}
