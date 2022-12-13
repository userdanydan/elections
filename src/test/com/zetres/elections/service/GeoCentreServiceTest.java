package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.repos.GeoCentreRepository;
import com.zetres.elections.repos.GeoLocaliteRepository;
import org.hibernate.envers.AuditReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GeoCentreServiceTest {

    @Mock private GeoCentreRepository geoCentreRepository;
    @Mock private GeoLocaliteRepository geoLocaliteRepository;
    @Mock private AuditReader reader;
    @InjectMocks private GeoCentreService geoCentreService;

    private GeoCentre geoCentre1, geoCentre2;
    private GeoLocalite geoLocalite1, geoLocalite2;

    @BeforeEach
    void setUp() {
        geoLocalite1 = new GeoLocalite();
        geoLocalite1.setName("AFADADE-NIMA");
        geoLocalite1.setPk("7A3EE81E798454FAE0501EAC02326524");
        geoLocalite1.setCode("01");

        geoLocalite1 = new GeoLocalite();
        geoLocalite1.setName("AFADADE-NIMA 2");
        geoLocalite1.setPk("7A3EE81E798454FAE0501EAC02326525");
        geoLocalite1.setCode("02");

        geoCentre1 = new GeoCentre();
        geoCentre1.setName("EPP KABRE-FATIMA");
        geoCentre1.setPk("7A3EE81E8D3D54FAE0501EAC02326524");
        geoCentre1.setKitidbegin("2-001-01-03-01-01");

        geoCentre2 = new GeoCentre();
        geoCentre2.setName("New Centre");
        geoCentre2.setPk("TESTE81E8D3D54FAE0501EAC02326524");
        geoCentre2.setKitidbegin("2-001-01-03-01-01");
    }

    @Test
    void moveCenterToANewLocation(){
        var expected = geoLocalite2;
        geoCentre1.setFkLocalite(geoLocalite1);
        geoCentreService.moveCentre(geoCentre1, geoLocalite2);
        var actual = geoCentre1.getFkLocalite();
        assertEquals(expected, actual);
    }

    @Test
    void whenMoveCentreToANewLocation_itShouldBeRecordedAsAMoveEvent(){
        geoCentre1.setFkLocalite(geoLocalite1);
        geoCentreService.moveCentre(geoCentre1, geoLocalite2);


    }

    @Test
    void mergeTwoCentersIntoOneLocation(){
        geoCentreService.mergeCentres(geoLocalite1, geoCentre1, geoCentre2);
        var expected = geoLocalite1;
        var actual = geoCentre1.getFkLocalite();
        assertEquals(expected, actual);
        actual = geoCentre2.getFkLocalite();
        assertEquals(expected, actual);
    }

    @Test
    void mergeTwoCentersIntoOneLocationAndVerifyItWasAudited(){
        geoCentreService.mergeCentres(geoLocalite1, geoCentre1, geoCentre2);

    }

    @Test
    void whenMoveACenter_ItShouldBeDeletedAndAnNewOneIsCreatedWithATraceInTheRepository(){

    }
    @Test
    void whenCreateANewCenter_ItShouldHaveAUniqueCodeForTheKit(){

    }

    @AfterEach
    void tearDown() {
        geoCentre1 = null;
        geoCentre2 = null;
        geoLocalite1 = null;
    }
}