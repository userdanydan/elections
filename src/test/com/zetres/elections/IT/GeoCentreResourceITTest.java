package com.zetres.elections.IT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zetres.elections.domain.*;
import com.zetres.elections.model.*;
import com.zetres.elections.service.GeoCentreService;
import com.zetres.elections.service.GeoLocaliteService;
import com.zetres.elections.service.GeoRegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback(true)
public class GeoCentreResourceITTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private GeoCentreService geoCentreService;
    @Autowired
    private GeoLocaliteService geoLocaliteService;
    private final String url = "http://localhost:";
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
    @Autowired
    private GeoRegionService geoRegionService;

    private final String uuidRandom = UUID.randomUUID().toString().toUpperCase();

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
    void whenGetARegion_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        var expected = regionDTO.getPk();
        var centreFromRest = restTemplate.getForObject(url+port+"/api/geoRegions/"+regionDTO.getPk(), GeoRegion.class);
        var actual = centreFromRest.getPk();
        assertEquals(expected, actual);
    }

    @Test
    void whenGetAPrefecture_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        System.err.println(prefectureDTO.getPk());
        var result = restTemplate.postForObject(url+port+"/api/geoPrefectures", prefectureDTO, String.class);
        System.err.println("*********"+result+"*******");
        var expected = "{\"pk\":3106,\"code\":\"106\",\"name\":\"DOUFELGOU\",\"shortName\":null,\"active\":null,\"fkRegion\":1}";
        prefectureDTO.setFkRegion(regionDTO.getPk());
        var prefectureFromRest = restTemplate.getForObject(url+port+"/api/geoPrefectures/"+prefectureDTO.getPk(), String.class);
        var actual = prefectureFromRest;
        assertEquals(expected, actual);
    }

    @Test
    void whenGetACommune_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoPrefectures", prefectureDTO, String.class);
        var result = restTemplate.postForObject(url+port+"/api/geoCommunes", communeDTO, String.class);
        System.err.println(result);
        var expected = "{\"pk\":310601,\"code\":\"01\",\"name\":\"DOUFELGOU 1\",\"active\":null,\"fkPrefecture\":3106}";
        var communeFromRest = restTemplate.getForObject(url+port+"/api/geoCommunes/"+communeDTO.getPk(), String.class);
        var actual = communeFromRest;
        assertEquals(expected, actual);
    }

    @Test
    void whenGetACanton_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoPrefectures", prefectureDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCommunes", communeDTO, String.class);
        var result = restTemplate.postForObject(url+port+"/api/geoCantons/", cantonDTO, String.class);
        var expected = "{\"pk\":31060106,\"code\":\"06\",\"name\":\"SIOU\",\"active\":null,\"fkCommune\":310601}";
        var cantonFromRest = restTemplate.getForObject(url+port+"/api/geoCantons/"+cantonDTO.getPk(), String.class);
        var actual = cantonFromRest;
        assertEquals(expected, actual);
    }

    @Test
    void whenGetALocalite_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoPrefectures", prefectureDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCommunes", communeDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCantons/", cantonDTO, String.class);
        var result = restTemplate.postForObject(url+port+"/api/geoLocalites/", localiteDTO, String.class);
        var expected = "{\"pk\":\"7A3EE81E85E954FAE0501EAC02326524\",\"code\":\"12\",\"name\":\"KPALOWA\",\"dateAdded\":null,\"active\":null,\"fkCanton\":31060106}";
        var localiteFromRest = restTemplate.getForObject(url+port+"/api/geoLocalites/"+localiteDTO.getPk(), String.class);
        var actual = localiteFromRest;
        assertEquals(expected, actual);
    }

    @Test
    void whenGetACenter_ItShouldBePresent(){
        restTemplate.postForObject(url+port+"/api/geoRegions", regionDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoPrefectures", prefectureDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCommunes", communeDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCantons/", cantonDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoLocalites/", localiteDTO, String.class);
        var result = restTemplate.postForObject(url+port+"/api/geoCentres/", centreDTO, String.class);
        var expected = "\"7A3EE81E897C54FAE0501EAC02326524\"";
        var centreFromRest = restTemplate.getForObject(url+port+"/api/geoCentres/"+centreDTO.getPk(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            var node = mapper.readTree(centreFromRest);
            var actual = node.get("pk").toString();
            assertEquals(expected, actual);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenAnExistingCentre_whenGetACentreByName_thenCentresWithAllGeographicalHierarchy() throws Exception {
        //arrange
        var expected = "\"7A3EE81E897C54FAE0501EAC02326524\"";
        restTemplate.postForObject(url+port+"/api/geoRegions/", regionDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoPrefectures/", prefectureDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCommunes/", communeDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCantons/", cantonDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoLocalites/", localiteDTO, String.class);
        restTemplate.postForObject(url+port+"/api/geoCentres/", centreDTO, String.class);
        //act
        var centreFromRest = restTemplate.getForObject(url+port+"/api/geoCentres/search/"+geoCentre.getName(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var node = mapper.readTree(centreFromRest);
        //assert
        assertTrue(node.isArray());
        var firstValueFromArray = node.get(0);
        var actual = firstValueFromArray.get("pk").toString();
        assertEquals(expected, actual);
    }
}