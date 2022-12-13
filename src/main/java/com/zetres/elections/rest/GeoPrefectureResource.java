package com.zetres.elections.rest;

import com.zetres.elections.model.GeoPrefectureDTO;
import com.zetres.elections.service.GeoPrefectureService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/geoPrefectures", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoPrefectureResource {

    private final GeoPrefectureService geoPrefectureService;

    public GeoPrefectureResource(final GeoPrefectureService geoPrefectureService) {
        this.geoPrefectureService = geoPrefectureService;
    }

    @GetMapping
    public ResponseEntity<List<GeoPrefectureDTO>> getAllGeoPrefectures() {
        return ResponseEntity.ok(geoPrefectureService.findAll());
    }

    @GetMapping("/{pk}")
    public ResponseEntity<GeoPrefectureDTO> getGeoPrefecture(@PathVariable final Integer pk) {
        return ResponseEntity.ok(geoPrefectureService.get(pk));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createGeoPrefecture(
            @RequestBody @Valid final GeoPrefectureDTO geoPrefectureDTO) {
        return new ResponseEntity<>(geoPrefectureService.create(geoPrefectureDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{pk}")
    public ResponseEntity<Void> updateGeoPrefecture(@PathVariable final Integer pk,
            @RequestBody @Valid final GeoPrefectureDTO geoPrefectureDTO) {
        geoPrefectureService.update(pk, geoPrefectureDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pk}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeoPrefecture(@PathVariable final Integer pk) {
        geoPrefectureService.delete(pk);
        return ResponseEntity.noContent().build();
    }

}
