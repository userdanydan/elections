package com.zetres.elections.service;

import com.zetres.elections.domain.GeoPrefecture;
import com.zetres.elections.domain.GeoRegion;
import com.zetres.elections.model.GeoPrefectureDTO;
import com.zetres.elections.repos.GeoPrefectureRepository;
import com.zetres.elections.repos.GeoRegionRepository;
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
public class GeoPrefectureService {

    private final GeoPrefectureRepository geoPrefectureRepository;
    private final GeoRegionRepository geoRegionRepository;

    public GeoPrefectureService(final GeoPrefectureRepository geoPrefectureRepository,
            final GeoRegionRepository geoRegionRepository) {
        this.geoPrefectureRepository = geoPrefectureRepository;
        this.geoRegionRepository = geoRegionRepository;
    }

    public List<GeoPrefectureDTO> findAll() {
        return geoPrefectureRepository.findAll(Sort.by("pk"))
                .stream()
                .map(geoPrefecture -> mapToDTO(geoPrefecture, new GeoPrefectureDTO()))
                .collect(Collectors.toList());
    }

    public GeoPrefectureDTO get(final Integer pk) {
        return geoPrefectureRepository.findById(pk)
                .map(geoPrefecture -> mapToDTO(geoPrefecture, new GeoPrefectureDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @Transactional
    public Integer create(final GeoPrefectureDTO geoPrefectureDTO) {
        final GeoPrefecture geoPrefecture = new GeoPrefecture();
        mapToEntity(geoPrefectureDTO, geoPrefecture);
        var prefectureCodesWithinRegionSet =
                MiscUtils.getUniqueCodesSetPrefecture(
                        this.geoPrefectureRepository
                                .findByFkRegion(geoPrefecture
                                        .getFkRegion()
                                        .getPk()
                                )
                );
        boolean isNotADoublon = prefectureCodesWithinRegionSet.add(geoPrefectureDTO.getCode());
        if(!isNotADoublon) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A code must be unique for a given prefecture.");
        }
        return geoPrefectureRepository.save(geoPrefecture).getPk();
    }

    public void update(final Integer pk, final GeoPrefectureDTO geoPrefectureDTO) {
        final GeoPrefecture geoPrefecture = geoPrefectureRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoPrefectureDTO, geoPrefecture);
        geoPrefectureRepository.save(geoPrefecture);
    }

    public void delete(final Integer pk) {
        geoPrefectureRepository.deleteById(pk);
    }

    private GeoPrefectureDTO mapToDTO(final GeoPrefecture geoPrefecture,
            final GeoPrefectureDTO geoPrefectureDTO) {
        geoPrefectureDTO.setPk(geoPrefecture.getPk());
        geoPrefectureDTO.setCode(geoPrefecture.getCode());
        geoPrefectureDTO.setName(geoPrefecture.getName());
        geoPrefectureDTO.setShortName(geoPrefecture.getShortName());
        geoPrefectureDTO.setActive(geoPrefecture.getActive());
        geoPrefectureDTO.setFkRegion(geoPrefecture.getFkRegion() == null ? null : geoPrefecture.getFkRegion().getPk());
        return geoPrefectureDTO;
    }

    private GeoPrefecture mapToEntity(final GeoPrefectureDTO geoPrefectureDTO,
            final GeoPrefecture geoPrefecture) {
        geoPrefecture.setPk(geoPrefectureDTO.getPk());
        geoPrefecture.setCode(geoPrefectureDTO.getCode());
        geoPrefecture.setName(geoPrefectureDTO.getName());
        geoPrefecture.setShortName(geoPrefectureDTO.getShortName());
        geoPrefecture.setActive(geoPrefectureDTO.getActive());
        final GeoRegion fkRegion = geoPrefectureDTO.getFkRegion() == null ? null : geoRegionRepository.findById(geoPrefectureDTO.getFkRegion())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "fkRegion not found"));
        geoPrefecture.setFkRegion(fkRegion);
        return geoPrefecture;
    }

    @Transactional
    public String getReferencedWarning(final Integer pk) {
        final GeoPrefecture geoPrefecture = geoPrefectureRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!geoPrefecture.getFkPrefectureGeoCommunes().isEmpty()) {
            return WebUtils.getMessage("geoPrefecture.geoCommune.manyToOne.referenced", geoPrefecture.getFkPrefectureGeoCommunes().iterator().next().getPk());
        }
        return null;
    }

}
