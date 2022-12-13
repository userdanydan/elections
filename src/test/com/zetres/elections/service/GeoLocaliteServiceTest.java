package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCanton;
import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.model.GeoLocaliteDTO;
import com.zetres.elections.repos.GeoCantonRepository;
import com.zetres.elections.repos.GeoCentreRepository;
import com.zetres.elections.repos.GeoLocaliteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static com.zetres.elections.util.MiscUtils.getUniqueCodesSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class GeoLocaliteServiceTest {

    @Mock private GeoCentreRepository geoCentreRepository;
    @Mock private GeoLocaliteRepository geoLocaliteRepository;
    @Mock private GeoCantonRepository geoCantonRepository;

    @InjectMocks private GeoLocaliteService geoLocaliteService;

    private GeoCentre geoCentre1, geoCentre2;
    private GeoLocalite geoLocalite1, geoLocalite2;
    private GeoCanton geoCanton;

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

        geoCanton = new GeoCanton();
        geoCanton.setName("New Canton");
        geoCanton.setPk(1);
        geoCanton.setCode("1");
    }


    @Test
    void whenCreateALocalite_ItShouldHaveAUniqueCodeWithinTheCantonInstance(){
        //Arrange
        lenient().doThrow(new RuntimeException()).when(geoLocaliteRepository).save(geoLocalite2);
        //Act
        geoLocalite2.setCode(geoLocalite1.getCode());
        //Assert
        assertThrows(RuntimeException.class,
                ()->geoLocaliteService.create(mapToDTO(geoLocalite2, new GeoLocaliteDTO())),
                "It should have sent an exception for not unique code within the same canton instance.");
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

    @Test
    void testGetUniqueCode() {
        var geoLocalite1 = new GeoLocalite();
        geoLocalite1.setName("test1");
        geoLocalite1.setCode("01");

        var geoLocalite2 = new GeoLocalite();
        geoLocalite2.setName("test2");
        geoLocalite2.setCode("02");

        var geoLocalite3 = new GeoLocalite();
        geoLocalite1.setName("test3");
        geoLocalite1.setCode("03");

        var geoLocalite4 = new GeoLocalite();
        geoLocalite2.setName("test4");
        geoLocalite2.setCode("04");

        try {
            Set<String> setOfUniqueCodes = getUniqueCodesSet(
                                                            List.of(
                                                                geoLocalite1,
                                                                geoLocalite2,
                                                                geoLocalite3,
                                                                geoLocalite4));
            var uniqueCode = geoLocaliteService.getUniqueCode(setOfUniqueCodes);
            var isUnique = setOfUniqueCodes.add(uniqueCode);
            assertTrue(isUnique);
        }catch(Exception e){
            fail("It should have create a set of unique set of value");
        }
    }


    @Test
    void testFormattedValueCode(){
        var expected = "01";
        var actual = geoLocaliteService.toFormattedValueCode(1);
        assertEquals(expected, actual);
    }
    @Test
    void testFormatedValueVodeWith10(){
        var expected = "10";
        var actual = geoLocaliteService.toFormattedValueCode(10);
        assertEquals(expected, actual);
    }
    @Test
    void testFormatedValueVodeWith0(){
        var expected = "00";
        var actual = geoLocaliteService.toFormattedValueCode(0);
        assertEquals(expected, actual);
    }
}