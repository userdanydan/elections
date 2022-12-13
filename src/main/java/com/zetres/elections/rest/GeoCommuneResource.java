package com.zetres.elections.rest;

import com.zetres.elections.model.GeoCommuneDTO;
import com.zetres.elections.service.GeoCommuneService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/geoCommunes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoCommuneResource {

    private final GeoCommuneService geoCommuneService;

    public GeoCommuneResource(final GeoCommuneService geoCommuneService) {
        this.geoCommuneService = geoCommuneService;
    }

    @GetMapping
    public ResponseEntity<List<GeoCommuneDTO>> getAllGeoCommunes() {
        return ResponseEntity.ok(geoCommuneService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoCommuneDTO> getGeoCommune(@PathVariable final Integer pk) {
        return ResponseEntity.ok(geoCommuneService.get(pk));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createGeoCommune(
            @RequestBody @Valid final GeoCommuneDTO geoCommuneDTO) {
        return new ResponseEntity<>(geoCommuneService.create(geoCommuneDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoCommune(@PathVariable final Integer pk,
            @RequestBody @Valid final GeoCommuneDTO geoCommuneDTO) {
        geoCommuneService.update(pk, geoCommuneDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoCommune(@PathVariable final Integer pk) {
        geoCommuneService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
