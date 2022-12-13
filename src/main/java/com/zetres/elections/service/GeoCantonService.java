package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCanton;
import com.zetres.elections.domain.GeoCommune;
import com.zetres.elections.model.GeoCantonDTO;
import com.zetres.elections.repos.GeoCantonRepository;
import com.zetres.elections.repos.GeoCommuneRepository;
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
public class GeoCantonService {

    private final GeoCantonRepository geoCantonRepository;
    private final GeoCommuneRepository geoCommuneRepository;

    public GeoCantonService(final GeoCantonRepository geoCantonRepository,
            final GeoCommuneRepository geoCommuneRepository) {
        this.geoCantonRepository = geoCantonRepository;
        this.geoCommuneRepository = geoCommuneRepository;
    }

    public List<GeoCantonDTO> findAll() {
        return geoCantonRepository.findAll(Sort.by("pk"))
                .stream()
                .map(geoCanton -> mapToDTO(geoCanton, new GeoCantonDTO()))
                .collect(Collectors.toList());
    }

    public GeoCantonDTO get(final Integer pk) {
        return geoCantonRepository.findById(pk)
                .map(geoCanton -> mapToDTO(geoCanton, new GeoCantonDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Integer create(final GeoCantonDTO geoCantonDTO) {
        final GeoCanton geoCanton = new GeoCanton();
        mapToEntity(geoCantonDTO, geoCanton);
        var cantonCodesWithinCommuneSet =
                MiscUtils.getUniqueCodesSetCanton(
                        this.geoCantonRepository
                                .findByFkCommune(geoCanton
                                        .getFkCommune()
                                        .getPk()
                                )
                );
        boolean isNotADoublon = cantonCodesWithinCommuneSet.add(geoCantonDTO.getCode());
        if(!isNotADoublon) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A code must be unique for a given commune.");
        }
        return geoCantonRepository.save(geoCanton).getPk();
    }

    public void update(final Integer pk, final GeoCantonDTO geoCantonDTO) {
        final GeoCanton geoCanton = geoCantonRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoCantonDTO, geoCanton);
        geoCantonRepository.save(geoCanton);
    }

    public void delete(final Integer pk) {
        geoCantonRepository.deleteById(pk);
    }

    private GeoCantonDTO mapToDTO(final GeoCanton geoCanton, final GeoCantonDTO geoCantonDTO) {
        geoCantonDTO.setPk(geoCanton.getPk());
        geoCantonDTO.setCode(geoCanton.getCode());
        geoCantonDTO.setName(geoCanton.getName());
        geoCantonDTO.setActive(geoCanton.getActive());
        geoCantonDTO.setFkCommune(geoCanton.getFkCommune() == null ? null : geoCanton.getFkCommune().getPk());
        return geoCantonDTO;
    }

    private GeoCanton mapToEntity(final GeoCantonDTO geoCantonDTO, final GeoCanton geoCanton) {
        geoCanton.setPk(geoCantonDTO.getPk());
        geoCanton.setCode(geoCantonDTO.getCode());
        geoCanton.setName(geoCantonDTO.getName());
        geoCanton.setActive(geoCantonDTO.getActive());
        final GeoCommune fkCommune = geoCantonDTO.getFkCommune() == null ? null : geoCommuneRepository.findById(geoCantonDTO.getFkCommune())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "fkCommune not found"));
        geoCanton.setFkCommune(fkCommune);
        return geoCanton;
    }

    @Transactional
    public String getReferencedWarning(final Integer pk) {
        final GeoCanton geoCanton = geoCantonRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!geoCanton.getFkCantonGeoLocalites().isEmpty()) {
            return WebUtils.getMessage("geoCanton.geoLocalite.manyToOne.referenced", geoCanton.getFkCantonGeoLocalites().iterator().next().getPk());
        }
        return null;
    }

}
