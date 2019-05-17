package sg.com.wego.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.exception.FareFlightException;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.validator.FareFlightSearchValidatorImpl;
import sg.com.wego.validator.ValidatorHelper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorHelperTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    FareFlightSearchValidatorImpl fareFlightSearchValidator;

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_IsTheSame() {

        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        assertThrows(FareFlightException.class, () -> ValidatorHelper.of(metaSearchCriteria).validate(fareFlightSearchValidator::isDepartureAndArriValNotSame, "Exception_1").get());
    }

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_NotOnSchedules() {

        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AFFFFFF");

        when(scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(anyString(), anyString())).thenReturn(new ArrayList<>());

        assertThrows(FareFlightException.class, () -> ValidatorHelper.of(metaSearchCriteria).validate(fareFlightSearchValidator::isDepartureAndArrivalExistsOnSchudules, "Exception_1").get());
    }

    @Test
    public void shouldThrowException_WhenDepartCodeAndArrivalCode_NotSupported() {

        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AFFFFFF");

        when(airportRepository.findAllByCode(anyString())).thenReturn(new ArrayList<>());

        assertThrows(FareFlightException.class, () -> ValidatorHelper.of(metaSearchCriteria).validate(fareFlightSearchValidator::isDepartureAndArrivalCodeSupported, "Exception_1").get());
    }

}
