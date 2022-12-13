package com.zetres.elections.repos;

import com.zetres.elections.domain.GeoCentre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface GeoCentresAuditRepository extends CrudRepository<GeoCentre, String>, RevisionRepository<GeoCentre, String, Long> {
}
