package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoRegion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GeoRegionRepository extends JpaRepository<GeoRegion, Integer> {
}
