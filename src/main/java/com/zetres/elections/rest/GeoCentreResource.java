package com.zetres.elections.rest;

import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.model.GeoCentreDTO;
import com.zetres.elections.service.GeoCentreService;
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
@RequestMapping(value = "/api/geoCentres", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoCentreResource {

    private final GeoCentreService geoCentreService;

    public GeoCentreResource(final GeoCentreService geoCentreService) {
        this.geoCentreService = geoCentreService;
    }

    @GetMapping
    public ResponseEntity<List<GeoCentreDTO>> getAllGeoCentres() {
        return ResponseEntity.ok(geoCentreService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoCentreDTO> getGeoCentre(@PathVariable final String pk) {
        return ResponseEntity.ok(geoCentreService.get(pk));
    }

    @GetMapping("/search/{name}")
    public GeoCentre[] getGeoCentreByName(@PathVariable final String name){
        return geoCentreService.findByName(name);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Void> createGeoCentre(@RequestBody @Valid final GeoCentreDTO geoCentreDTO,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("pk") && geoCentreDTO.getPk() == null) {
            bindingResult.rejectValue("pk", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("pk") && geoCentreService.pkExists(geoCentreDTO.getPk())) {
            bindingResult.rejectValue("pk", "Exists.geoCentre.pk");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        geoCentreService.create(geoCentreDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoCentre(@PathVariable final String pk,
            @RequestBody @Valid final GeoCentreDTO geoCentreDTO) {
        geoCentreService.update(pk, geoCentreDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoCentre(@PathVariable final String pk) {
        geoCentreService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
