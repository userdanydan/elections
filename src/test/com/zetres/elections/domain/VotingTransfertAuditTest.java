package com.zetres.elections.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VotingTransfertAuditTest {
    private GeoCentre geoCentre1, geoCentre2;
    private GeoLocalite geoLocalite1, geoLocalite2;

    private VotingTransfertAudit votingTransfertAudit;
    @BeforeEach
    void setUp() {
        geoLocalite1 = new GeoLocalite();
        geoLocalite1.setName("AFADADE-NIMA");
        geoLocalite1.setPk("7A3EE81E798454FAE0501EAC02326524");
        geoLocalite1.setCode("01");

        geoLocalite2 = new GeoLocalite();
        geoLocalite2.setName("AFADADE-NIMA 2");
        geoLocalite2.setPk("7A3EE81E798454FAE0501EAC02326525");
        geoLocalite2.setCode("02");

        geoCentre1 = new GeoCentre();
        geoCentre1.setName("EPP KABRE-FATIMA");
        geoCentre1.setPk("7A3EE81E8D3D54FAE0501EAC02326524");
        geoCentre1.setKitidbegin("2-001-01-03-01-01");

        geoCentre2 = new GeoCentre();
        geoCentre2.setName("New Centre");
        geoCentre2.setPk("TESTE81E8D3D54FAE0501EAC02326524");
        geoCentre2.setKitidbegin("2-001-01-03-01-01");

        votingTransfertAudit = new VotingTransfertAudit();
    }
    @Test
    void testMoveACentreInANewLocation(){
        //Arrange
        geoCentre1.setFkLocalite(geoLocalite1);
        //Act
        geoCentre1.setFkLocalite(geoLocalite2);
        //Assert
        var expected = geoLocalite2.getName();
        var actual = geoCentre1.getFkLocalite().getName();
        assertEquals(expected, actual);
    }

}

