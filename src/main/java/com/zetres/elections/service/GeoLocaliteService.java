package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCanton;
import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.model.GeoLocaliteDTO;
import com.zetres.elections.repos.GeoCantonRepository;
import com.zetres.elections.repos.GeoLocaliteRepository;
import com.zetres.elections.util.MiscUtils;
import com.zetres.elections.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class GeoLocaliteService {

    private final GeoLocaliteRepository geoLocaliteRepository;
    private final GeoCantonRepository geoCantonRepository;

    public GeoLocaliteService(final GeoLocaliteRepository geoLocaliteRepository,
            final GeoCantonRepository geoCantonRepository) {
        this.geoLocaliteRepository = geoLocaliteRepository;
        this.geoCantonRepository = geoCantonRepository;
    }

    public List<GeoLocaliteDTO> findAll() {
        return geoLocaliteRepository.findAll(Sort.by("pk"))
                .stream()
                .map(geoLocalite -> mapToDTO(geoLocalite, new GeoLocaliteDTO()))
                .collect(Collectors.toList());
    }

    public List<GeoLocaliteDTO> findByCantonId(final Integer cantonId){
        return geoLocaliteRepository.findByFkCanton(cantonId)
                .stream()
                .map(geoLocalite -> mapToDTO(geoLocalite, new GeoLocaliteDTO()))
                .collect(Collectors.toList());
    }

    public GeoLocaliteDTO get(final String pk) {
        return geoLocaliteRepository.findById(pk)
                .map(geoLocalite -> mapToDTO(geoLocalite, new GeoLocaliteDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public String create(final GeoLocaliteDTO geoLocaliteDTO) {
        final GeoLocalite geoLocalite = new GeoLocalite();
        mapToEntity(geoLocaliteDTO, geoLocalite);
        geoLocalite.setPk(geoLocaliteDTO.getPk());
        var locationCodesWithinCantonSet =
                MiscUtils.getUniqueCodesSet(
                        this.geoLocaliteRepository
                        .findByFkCanton(geoLocalite
                                .getFkCanton()
                                .getPk()
                        )
                );
        boolean isNotADoublon = locationCodesWithinCantonSet.add(geoLocaliteDTO.getCode());
        if(!isNotADoublon) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A code must be unique for a given canton.");
        }
        return geoLocaliteRepository.save(geoLocalite).getPk();
    }

    public void update(final String pk, final GeoLocaliteDTO geoLocaliteDTO) {
        final GeoLocalite geoLocalite = geoLocaliteRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoLocaliteDTO, geoLocalite);
        geoLocaliteRepository.save(geoLocalite);
    }

    public void delete(final String pk) {
        geoLocaliteRepository.deleteById(pk);
    }

    private GeoLocaliteDTO mapToDTO(final GeoLocalite geoLocalite,
            final GeoLocaliteDTO geoLocaliteDTO) {
        geoLocaliteDTO.setPk(geoLocalite.getPk());
        geoLocaliteDTO.setCode(geoLocalite.getCode());
        geoLocaliteDTO.setName(geoLocalite.getName());
        geoLocaliteDTO.setDateAdded(geoLocalite.getDateAdded());
        geoLocaliteDTO.setActive(geoLocalite.getActive());
        geoLocaliteDTO.setFkCanton(geoLocalite.getFkCanton() == null ? null : geoLocalite.getFkCanton().getPk());
        return geoLocaliteDTO;
    }

    private GeoLocalite mapToEntity(final GeoLocaliteDTO geoLocaliteDTO,
            final GeoLocalite geoLocalite) {
        geoLocalite.setPk(geoLocaliteDTO.getPk());
        geoLocalite.setCode(geoLocaliteDTO.getCode());
        geoLocalite.setName(geoLocaliteDTO.getName());
        geoLocalite.setDateAdded(geoLocaliteDTO.getDateAdded());
        geoLocalite.setActive(geoLocaliteDTO.getActive());
        final GeoCanton fkCanton = geoLocaliteDTO.getFkCanton() == null ? null : geoCantonRepository.findById(geoLocaliteDTO.getFkCanton())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "fkCanton not found"));
        geoLocalite.setFkCanton(fkCanton);
        return geoLocalite;
    }

    public boolean pkExists(final String pk) {
        return geoLocaliteRepository.existsByPkIgnoreCase(pk);
    }

    @Transactional
    public String getReferencedWarning(final String pk) {
        final GeoLocalite geoLocalite = geoLocaliteRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!geoLocalite.getFkLocaliteGeoCentres().isEmpty()) {
            return WebUtils.getMessage("geoLocalite.geoCentre.manyToOne.referenced", geoLocalite.getFkLocaliteGeoCentres().iterator().next().getPk());
        }
        return null;
    }
    public String getUniqueCode(Set<String> setOfUniqueCode) {
        var copyOfSet = new HashSet<>(setOfUniqueCode);
        var isUniqueCode = true;
        var valueCodeToAdd = -1L;
        String valueCodeToAddStr;
        do{
            valueCodeToAdd++;
            valueCodeToAddStr = toFormattedValueCode(valueCodeToAdd);
            isUniqueCode = copyOfSet.add(valueCodeToAddStr);
        } while (!isUniqueCode);
        return valueCodeToAddStr;
    }
    public String toFormattedValueCode(long valueCodeToAdd) {
        String result;
        if(valueCodeToAdd<10){
            result = String.format("%02d", valueCodeToAdd);
        }else{
            result = String.valueOf(valueCodeToAdd);
        }
        return result;
    }
}
