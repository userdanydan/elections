package com.zetres.elections.rest;

import com.zetres.elections.model.GeoCantonDTO;
import com.zetres.elections.service.GeoCantonService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/geoCantons", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoCantonResource {

    private final GeoCantonService geoCantonService;

    public GeoCantonResource(final GeoCantonService geoCantonService) {
        this.geoCantonService = geoCantonService;
    }

    @GetMapping
    public ResponseEntity<List<GeoCantonDTO>> getAllGeoCantons() {
        return ResponseEntity.ok(geoCantonService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoCantonDTO> getGeoCanton(@PathVariable final Integer pk) {
        return ResponseEntity.ok(geoCantonService.get(pk));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createGeoCanton(
            @RequestBody @Valid final GeoCantonDTO geoCantonDTO) {
        return new ResponseEntity<>(geoCantonService.create(geoCantonDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoCanton(@PathVariable final Integer pk,
            @RequestBody @Valid final GeoCantonDTO geoCantonDTO) {
        geoCantonService.update(pk, geoCantonDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoCanton(@PathVariable final Integer pk) {
        geoCantonService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
