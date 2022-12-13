package com.zetres.elections.rest;

import com.zetres.elections.model.GeoLocaliteDTO;
import com.zetres.elections.service.GeoLocaliteService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/geoLocalites", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoLocaliteResource {

    private final GeoLocaliteService geoLocaliteService;

    public GeoLocaliteResource(final GeoLocaliteService geoLocaliteService) {
        this.geoLocaliteService = geoLocaliteService;
    }

    @GetMapping
    public ResponseEntity<List<GeoLocaliteDTO>> getAllGeoLocalites() {
        return ResponseEntity.ok(geoLocaliteService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoLocaliteDTO> getGeoLocalite(@PathVariable final String pk) {
        return ResponseEntity.ok(geoLocaliteService.get(pk));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Void> createGeoLocalite(
            @RequestBody @Valid final GeoLocaliteDTO geoLocaliteDTO,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("pk") && geoLocaliteDTO.getPk() == null) {
            bindingResult.rejectValue("pk", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("pk") && geoLocaliteService.pkExists(geoLocaliteDTO.getPk())) {
            bindingResult.rejectValue("pk", "Exists.geoLocalite.pk");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        geoLocaliteService.create(geoLocaliteDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoLocalite(@PathVariable final String pk,
            @RequestBody @Valid final GeoLocaliteDTO geoLocaliteDTO) {
        geoLocaliteService.update(pk, geoLocaliteDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoLocalite(@PathVariable final String pk) {
        geoLocaliteService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
