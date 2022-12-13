package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCommune;
import com.zetres.elections.domain.GeoPrefecture;
import com.zetres.elections.model.GeoCommuneDTO;
import com.zetres.elections.repos.GeoCommuneRepository;
import com.zetres.elections.repos.GeoPrefectureRepository;
import com.zetres.elections.util.MiscUtils;
import com.zetres.elections.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GeoCommuneService {

    private final GeoCommuneRepository geoCommuneRepository;
    private final GeoPrefectureRepository geoPrefectureRepository;

    public GeoCommuneService(final GeoCommuneRepository geoCommuneRepository,
            final GeoPrefectureRepository geoPrefectureRepository) {
        this.geoCommuneRepository = geoCommuneRepository;
        this.geoPrefectureRepository = geoPrefectureRepository;
    }

    public List<GeoCommuneDTO> findAll() {
        return geoCommuneRepository.findAll(Sort.by("pk"))
                .stream()
                .map(geoCommune -> mapToDTO(geoCommune, new GeoCommuneDTO()))
                .collect(Collectors.toList());
    }

    public GeoCommuneDTO get(final Integer pk) {
        return geoCommuneRepository.findById(pk)
                .map(geoCommune -> mapToDTO(geoCommune, new GeoCommuneDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Integer create(final GeoCommuneDTO geoCommuneDTO) {
        final GeoCommune geoCommune = new GeoCommune();
        mapToEntity(geoCommuneDTO, geoCommune);
        var communeCodesWithinPrefectureSet =
                MiscUtils.getUniqueCodesSetCommune(
                        this.geoCommuneRepository
                                .findByFkPrefecture(geoCommune
                                        .getFkPrefecture()
                                        .getPk()
                                )
                );
        boolean isNotADoublon = communeCodesWithinPrefectureSet.add(geoCommuneDTO.getCode());
        if(!isNotADoublon) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A code must be unique for a given commune.");
        }
        return geoCommuneRepository.save(geoCommune).getPk();
    }

    public void update(final Integer pk, final GeoCommuneDTO geoCommuneDTO) {
        final GeoCommune geoCommune = geoCommuneRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoCommuneDTO, geoCommune);
        geoCommuneRepository.save(geoCommune);
    }

    public void delete(final Integer pk) {
        geoCommuneRepository.deleteById(pk);
    }

    private GeoCommuneDTO mapToDTO(final GeoCommune geoCommune, final GeoCommuneDTO geoCommuneDTO) {
        geoCommuneDTO.setPk(geoCommune.getPk());
        geoCommuneDTO.setCode(geoCommune.getCode());
        geoCommuneDTO.setName(geoCommune.getName());
        geoCommuneDTO.setActive(geoCommune.getActive());
        geoCommuneDTO.setFkPrefecture(geoCommune.getFkPrefecture() == null ? null : geoCommune.getFkPrefecture().getPk());
        return geoCommuneDTO;
    }

    private GeoCommune mapToEntity(final GeoCommuneDTO geoCommuneDTO, final GeoCommune geoCommune) {
        geoCommune.setPk(geoCommuneDTO.getPk());
        geoCommune.setCode(geoCommuneDTO.getCode());
        geoCommune.setName(geoCommuneDTO.getName());
        geoCommune.setActive(geoCommuneDTO.getActive());
        final GeoPrefecture fkPrefecture = geoCommuneDTO.getFkPrefecture() == null ? null : geoPrefectureRepository.findById(geoCommuneDTO.getFkPrefecture())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "fkPrefecture not found"));
        geoCommune.setFkPrefecture(fkPrefecture);
        return geoCommune;
    }

    @Transactional
    public String getReferencedWarning(final Integer pk) {
        final GeoCommune geoCommune = geoCommuneRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!geoCommune.getFkCommuneGeoCantons().isEmpty()) {
            return WebUtils.getMessage("geoCommune.geoCanton.manyToOne.referenced", geoCommune.getFkCommuneGeoCantons().iterator().next().getPk());
        }
        return null;
    }

}
