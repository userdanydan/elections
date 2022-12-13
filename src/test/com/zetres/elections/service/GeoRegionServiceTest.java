package com.zetres.elections.service;

import com.zetres.elections.config.ConfigurationCentresElectorauxTest;
import org.hibernate.envers.AuditReader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
@Import(ConfigurationCentresElectorauxTest.class)
class GeoRegionServiceTest {
    @Mock
    private AuditReader reader;

    @Mock
    private Timestamp timestamp;

    @InjectMocks
    private GeoRegionService geoRegionService;

//    @Test
//    void testGetPresentVersion(){
//        var localDateTimeAn2000 = LocalDateTime.of(2000, Month.DECEMBER, 31, 0, 0, 0);
//        var timeStampAn2000 = timestamp.valueOf(localDateTimeAn2000);
//       // given(Timestamp.valueOf(localDateTimeAn2000)).willReturn(timeStampAn2000);
//        //given(reader.getRevisionNumberForDate(timeStampAn2000)).willReturn(83);
//        int expected = 83;
//        int actual = geoRegionService.versionOfTheCurrentHierarchicalGeoEntities();
//        assertEquals(expected, actual);
//    }
}