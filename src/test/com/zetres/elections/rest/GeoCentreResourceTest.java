package com.zetres.elections.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zetres.elections.domain.*;
import com.zetres.elections.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JsonTest
class GeoCentreResourceTest {
    @Autowired
    private JacksonTester<GeoCentreDTO> geoCentreDTOJacksonTester;
    @Autowired
    private JacksonTester<GeoCentre> geoCentreJacksonTester;
    @Autowired
    private ObjectMapper objectMapper;
    private GeoCentre geoCentre;
    private GeoCentreDTO centreDTO;
    private GeoLocalite geoLocalite;
    private GeoLocaliteDTO localiteDTO;
    private GeoCanton geoCanton;
    private GeoCantonDTO cantonDTO;
    private GeoCommune geoCommune;
    private GeoCommuneDTO communeDTO;
    private GeoPrefecture geoPrefecture;
    private GeoPrefectureDTO prefectureDTO;
    private GeoRegion geoRegion;
    private GeoRegionDTO regionDTO;

    @BeforeEach
    void setUp(){
        geoRegion = new GeoRegion();
        geoRegion.setPk(1);
        geoRegion.setName("MARITIME");

        regionDTO = new GeoRegionDTO();
        regionDTO.setPk(geoRegion.getPk());
        regionDTO.setName(geoRegion.getName());

        geoPrefecture = new GeoPrefecture();
        geoPrefecture.setPk(3106);
        geoPrefecture.setName("DOUFELGOU");
        geoPrefecture.setCode("106");
        geoPrefecture.setFkRegion(geoRegion);

        prefectureDTO = new GeoPrefectureDTO();
        prefectureDTO.setPk(geoPrefecture.getPk());
        prefectureDTO.setName(geoPrefecture.getName());
        prefectureDTO.setCode(geoPrefecture.getCode());
        prefectureDTO.setFkRegion(geoPrefecture.getFkRegion().getPk());

        geoCommune = new GeoCommune();
        geoCommune.setPk(310601);
        geoCommune.setName("DOUFELGOU 1");
        geoCommune.setCode("01");
        geoCommune.setFkPrefecture(geoPrefecture);

        communeDTO = new GeoCommuneDTO();
        communeDTO.setPk(geoCommune.getPk());
        communeDTO.setName(geoCommune.getName());
        communeDTO.setCode(geoCommune.getCode());
        communeDTO.setFkPrefecture(geoCommune.getFkPrefecture().getPk());

        geoCanton = new GeoCanton();
        geoCanton.setPk(31060106);
        geoCanton.setName("SIOU");
        geoCanton.setCode("06");
        geoCanton.setFkCommune(geoCommune);

        cantonDTO = new GeoCantonDTO();
        cantonDTO.setPk(geoCanton.getPk());
        cantonDTO.setName(geoCanton.getName());
        cantonDTO.setCode(geoCanton.getCode());
        cantonDTO.setFkCommune(geoCanton.getFkCommune().getPk());

        geoLocalite = new GeoLocalite();
        geoLocalite.setPk("7A3EE81E85E954FAE0501EAC02326524");
        geoLocalite.setName("KPALOWA");
        geoLocalite.setCode("12");
        geoLocalite.setFkCanton(geoCanton);

        localiteDTO = new GeoLocaliteDTO();
        localiteDTO.setPk(geoLocalite.getPk());
        localiteDTO.setName(geoLocalite.getName());
        localiteDTO.setCode(geoLocalite.getCode());
        localiteDTO.setFkCanton(geoLocalite.getFkCanton().getPk());

        geoCentre = new GeoCentre();
        geoCentre.setPk("7A3EE81E897C54FAE0501EAC02326524");
        geoCentre.setName("EPP KPALOWA");
        geoCentre.setKitidbegin("3-106-01-06-12-01");
        geoCentre.setFkLocalite(geoLocalite);

        centreDTO = new GeoCentreDTO();
        centreDTO.setPk(geoCentre.getPk());
        centreDTO.setName(geoCentre.getName());
        centreDTO.setKitidbegin(geoCentre.getKitidbegin());
        centreDTO.setFkLocalite(localiteDTO.getPk());
    }

    @Test
    void shouldObjectMapperBeInstantiated(){
        assertNotNull(objectMapper);
    }
    @Test
    void shouldSerializeACentre() throws IOException {
        var result = geoCentreDTOJacksonTester.write(centreDTO);
        System.err.println(result);
        assertThat(result).hasJsonPathStringValue("$.pk");
        assertThat(result).extractingJsonPathValue("$.name").isEqualTo(geoCentre.getName());
        assertThat(result).extractingJsonPathValue("$.fkLocalite").isEqualTo(geoCentre.getFkLocalite().getPk());
    }

    @Test
    void givenAnExistingCentre_whenSearchingByName_shouldResponseACenterWithAllGeoHierarchyInJSON() throws IOException {
        var result = geoCentreJacksonTester.write(geoCentre);
        System.err.println(result);
    }


}