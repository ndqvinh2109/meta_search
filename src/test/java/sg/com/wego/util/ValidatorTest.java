package sg.com.wego.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.exception.FareFlightException;
import sg.com.wego.service.MetaSearchCriteria;
import sg.com.wego.service.MetaSearchValidatorImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    MetaSearchValidatorImpl metaSearchValidator;

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_IsTheSame() {

        MetaSearchCriteria metaSearchCriteria = new MetaSearchCriteria();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        assertThrows(FareFlightException.class, () -> {
           Validator.of(metaSearchCriteria).validate(metaSearchValidator::isDepartureAndArriValNotSame, "Exception_1").get();
        });
    }

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_NotOnSchedules() {

        MetaSearchCriteria metaSearchCriteria = new MetaSearchCriteria();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AFFFFFF");

        when(scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(anyString(), anyString())).thenReturn(new ArrayList<>());

        assertThrows(FareFlightException.class, () -> {
            Validator.of(metaSearchCriteria).validate(metaSearchValidator::isDepartureAndArrivalExistsOnSchudules, "Exception_1").get();
        });
    }

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_NotSupported() {

        MetaSearchCriteria metaSearchCriteria = new MetaSearchCriteria();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AFFFFFF");

        when(airportRepository.findAllByCode(anyString())).thenReturn(new ArrayList<>());

        assertThrows(FareFlightException.class, () -> {
            Validator.of(metaSearchCriteria).validate(metaSearchValidator::isDepartureAndArrivalCodeSupported, "Exception_1").get();
        });
    }

}
