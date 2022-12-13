package com.zetres.elections.rest;

import com.zetres.elections.service.GeoCentresAuditService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/geoCentresAudit", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoCentresAuditResource {

    private final GeoCentresAuditService geoCentresAuditService;

    public GeoCentresAuditResource(final GeoCentresAuditService geoCentreService) {
        this.geoCentresAuditService = geoCentreService;
    }

    @GetMapping(value = "/{pk}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRevisionsByGeoCentres(@PathVariable String pk){
        return ResponseEntity.ok(geoCentresAuditService.findRevisions(pk));
    }

    @GetMapping(value = "/version/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRevisions(@PathVariable String version){
        return ResponseEntity.ok(geoCentresAuditService.getAllRevisionsAfterVersionId(version));
    }

}
