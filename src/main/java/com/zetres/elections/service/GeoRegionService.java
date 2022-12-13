package com.zetres.elections.service;

import com.zetres.elections.domain.GeoRegion;
import com.zetres.elections.model.GeoRegionDTO;
import com.zetres.elections.repos.BackupRecordsDAO;
import com.zetres.elections.repos.GeoRegionRepository;
import com.zetres.elections.util.WebUtils;
import org.hibernate.envers.AuditReader;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GeoRegionService {

    private final GeoRegionRepository geoRegionRepository;

    private final AuditReader auditReader;

    private final BackupRecordsDAO backupRecordsDAO;

    public GeoRegionService(final GeoRegionRepository geoRegionRepository, final AuditReader auditReader, final BackupRecordsDAO backupRecordsDAO) {
        this.geoRegionRepository = geoRegionRepository;
        this.auditReader = auditReader;
        this.backupRecordsDAO = backupRecordsDAO;
    }

    public List<GeoRegionDTO> findAll() {
        return geoRegionRepository.findAll(Sort.by("pk"))
                .stream()
                .map(geoRegion -> mapToDTO(geoRegion, new GeoRegionDTO()))
                .collect(Collectors.toList());
    }

    public GeoRegionDTO get(final Integer pk) {
        return geoRegionRepository.findById(pk)
                .map(geoRegion -> mapToDTO(geoRegion, new GeoRegionDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @Transactional
    public Integer create(final GeoRegionDTO geoRegionDTO) {
        final GeoRegion geoRegion = new GeoRegion();
        mapToEntity(geoRegionDTO, geoRegion);
        var pk = geoRegionRepository.findAll().size()+1;
        geoRegion.setPk(pk);
        return geoRegionRepository.save(geoRegion).getPk();
    }

    @Transactional
    public void update(final Integer pk, final GeoRegionDTO geoRegionDTO) {
        final GeoRegion geoRegion = geoRegionRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(geoRegionDTO, geoRegion);
        geoRegionRepository.save(geoRegion);
    }

    @Transactional
    public void delete(final Integer pk) {
        geoRegionRepository.deleteById(pk);
    }

    private GeoRegionDTO mapToDTO(final GeoRegion geoRegion, final GeoRegionDTO geoRegionDTO) {
        geoRegionDTO.setPk(geoRegion.getPk());
        geoRegionDTO.setName(geoRegion.getName());
        geoRegionDTO.setActive(geoRegion.getActive());
        return geoRegionDTO;
    }

    private GeoRegion mapToEntity(final GeoRegionDTO geoRegionDTO, final GeoRegion geoRegion) {
        geoRegion.setName(geoRegionDTO.getName());
        geoRegion.setActive(geoRegionDTO.getActive());
        return geoRegion;
    }

    @Transactional
    public String getReferencedWarning(final Integer pk) {
        final GeoRegion geoRegion = geoRegionRepository.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!geoRegion.getFkRegionGeoPrefectures().isEmpty()) {
            return WebUtils.getMessage("geoRegion.geoPrefecture.manyToOne.referenced", geoRegion.getFkRegionGeoPrefectures().iterator().next().getPk());
        }
        return null;
    }

    public int versionOfTheCurrentHierarchicalGeoEntities() {
         return (int)auditReader
                .getRevisionNumberForDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    public void addVersionToFile(){
        this.backupRecordsDAO.insert("<-- VERSION "+ this.versionOfTheCurrentHierarchicalGeoEntities() + "-->");
    }
}