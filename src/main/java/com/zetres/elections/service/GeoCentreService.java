package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.model.GeoCentreDTO;
import com.zetres.elections.repos.GeoCentreRepository;
import com.zetres.elections.repos.GeoLocaliteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GeoCentreService {

    private final GeoCentreRepository geoCentreRepository;
    private final GeoLocaliteRepository geoLocaliteRepository;
    public GeoCentreService(final GeoCentreRepository geoCentreRepository,
            final GeoLocaliteRepository geoLocaliteRepository) {
        this.geoCentreRepository = geoCentreRepository;
        this.geoLocaliteRepository = geoLocaliteRepository;
    }

    public List<GeoCentreDTO> findAll() {
        return geoCentreRepository.findAll(Sort.by("dateAdded").descending())
                .stream()
                .map(geoCentre -> mapToDTO(geoCentre, new GeoCentreDTO()))
                .collect(Collectors.toList());
    }

    public GeoCentreDTO get(final String pk) {
        return geoCentreRepository.findById(pk)
                .map(geoCentre -> mapToDTO(geoCentre, new GeoCentreDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public GeoCentre create(final GeoCentreDTO geoCentreDTO) {
        final GeoCentre geoCentre = new GeoCentre();
        mapToEntity(geoCentreDTO, geoCentre);
        geoCentre.setPk(geoCentreDTO.getPk());
        var centresWithinThisLocaliteCount = geoCentre
                .getFkLocalite()
                .getFkLocaliteGeoCentres()
                .stream()
                .distinct()
                .count();
        var kit = geoCentre.getKitidbegin();
        geoCentre.setKitidbegin(kit + String.format("%02d", (centresWithinThisLocaliteCount + 1)));
        geoCentre.setFullkitidbegin(geoCentre.getKitidbegin());
        geoCentre.setDateAdded(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        geoCentre.setName(geoCentre.getName().toUpperCase());
        return geoCentreRepository.save(geoCentre);
    }

    public void update(final String pk, final GeoCentreDTO geoCentreDTO) {
        final GeoCentre geoCentre = geoCentreRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoCentreDTO, geoCentre);
        geoCentreRepository.save(geoCentre);
    }

    public void delete(final String pk) {
        geoCentreRepository.deleteById(pk);
    }

    private GeoCentreDTO mapToDTO(final GeoCentre geoCentre, final GeoCentreDTO geoCentreDTO) {
        geoCentreDTO.setPk(geoCentre.getPk());
        geoCentreDTO.setName(geoCentre.getName());
        geoCentreDTO.setKitidbegin(geoCentre.getKitidbegin());
        geoCentreDTO.setFullkitidbegin(geoCentre.getFullkitidbegin());
        geoCentreDTO.setDateAdded(geoCentre.getDateAdded());
        geoCentreDTO.setActive(geoCentre.getActive());
        geoCentreDTO.setFkLocalite(geoCentre.getFkLocalite() == null ? null : geoCentre.getFkLocalite().getPk());
        return geoCentreDTO;
    }

    private GeoCentre mapToEntity(final GeoCentreDTO geoCentreDTO, final GeoCentre geoCentre) {
        geoCentre.setName(geoCentreDTO.getName());
        geoCentre.setKitidbegin(geoCentreDTO.getKitidbegin());
        geoCentre.setFullkitidbegin(geoCentreDTO.getFullkitidbegin());
        geoCentre.setDateAdded(geoCentreDTO.getDateAdded());
        geoCentre.setActive(geoCentreDTO.getActive());
        final GeoLocalite fkLocalite = geoCentreDTO.getFkLocalite() == null ? null : geoLocaliteRepository.findById(geoCentreDTO.getFkLocalite())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "fkLocalite not found"));
        geoCentre.setFkLocalite(fkLocalite);
        return geoCentre;
    }

    public boolean pkExists(final String pk) {
        return geoCentreRepository.existsByPkIgnoreCase(pk);
    }

    @Transactional
    public List<GeoCentreDTO> findByParentLieuId(String fkLocalite) {
        var geoCentreOpt = geoCentreRepository.findByFkLocalite(fkLocalite).stream()
                .map(geoCentre -> mapToDTO(geoCentre, new GeoCentreDTO())).toList();
        return geoCentreOpt;
    }

    public void mergeCentres(GeoLocalite geoLocalite, GeoCentre ... geoCentres) {
       for(var geoCentre : geoCentres){
           geoCentre.setFkLocalite(geoLocalite);
       }
    }

    public void moveCentre(GeoCentre geoCentre, GeoLocalite geoLocalite) {
        if(geoCentre!=null){
            if(!geoCentre.getFkLocalite().equals(geoLocalite)){
                geoCentre.setFkLocalite(geoLocalite);
            }
        }
    }

    public GeoCentre[] findByName(String name) {
        return geoCentreRepository.findByNameContainingIgnoreCase(name);
    }
}
