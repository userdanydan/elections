package com.zetres.elections.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoCentreTest {

    private GeoCentre geoCentre;
    private GeoLocalite geoLocalite;
    private GeoCanton geoCanton;
    private GeoCommune geoCommune;
    private GeoPrefecture geoPrefecture;
    private GeoRegion geoRegion;

    @BeforeEach
    void setUp() {
        geoRegion = new GeoRegion();
        geoRegion.setPk(1);
        geoRegion.setName("Region Test");

        geoPrefecture = new GeoPrefecture();
        geoPrefecture.setPk(1);
        geoPrefecture.setName("Prefecture Test");
        geoPrefecture.setFkRegion(geoRegion);

        geoCommune =  new GeoCommune();
        geoCommune.setPk(1);
        geoCommune.setName("Commune Test");
        geoCommune.setFkPrefecture(geoPrefecture);

        geoCanton = new GeoCanton();
        geoCanton.setPk(1);
        geoCanton.setName("Canton Test");
        geoCanton.setFkCommune(geoCommune);

        geoLocalite = new GeoLocalite();
        geoLocalite.setPk("1");
        geoLocalite.setName("Localite Test");
        geoLocalite.setFkCanton(geoCanton);

        geoCentre = new GeoCentre();
        geoCentre.setPk("1");
        geoCentre.setActive(0);
        geoCentre.setName("Centre Test");
        geoCentre.setKitidbegin("kit");
        geoCentre.setFullkitidbegin("fullkit");
        geoCentre.setDateAdded(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        geoCentre.setFkLocalite(geoLocalite);
    }

    @Test
    void testShowGeoHierarchy(){
        var expected = "Region Test > Prefecture Test > Commune Test > Canton Test > Localite Test > Centre Test";
        var actual = geoCentre.getGeoHierarchy();
        assertEquals(expected, actual);
    }

}