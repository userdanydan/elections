package com.zetres.elections.rest;

import com.zetres.elections.model.GeoRegionDTO;
import com.zetres.elections.service.GeoRegionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/geoRegions", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoRegionResource {

    private final GeoRegionService geoRegionService;

    public GeoRegionResource(final GeoRegionService geoRegionService) {
        this.geoRegionService = geoRegionService;
    }

    @GetMapping
    public ResponseEntity<List<GeoRegionDTO>> getAllGeoRegions() {
        return ResponseEntity.ok(geoRegionService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoRegionDTO> getGeoRegion(@PathVariable final Integer pk) {
        return ResponseEntity.ok(geoRegionService.get(pk));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createGeoRegion(
            @RequestBody @Valid final GeoRegionDTO geoRegionDTO) {
        return new ResponseEntity<>(geoRegionService.create(geoRegionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoRegion(@PathVariable final Integer pk,
            @RequestBody @Valid final GeoRegionDTO geoRegionDTO) {
        geoRegionService.update(pk, geoRegionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoRegion(@PathVariable final Integer pk) {
        geoRegionService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
