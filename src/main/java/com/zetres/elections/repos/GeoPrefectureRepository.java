package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoPrefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GeoPrefectureRepository extends JpaRepository<GeoPrefecture, Integer> {
    @Query("select g from GeoPrefecture g where g.fkRegion.pk = ?1")
    List<GeoPrefecture> findByFkRegion(Integer pk);
}
