package sg.com.wego.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import sg.com.wego.cache.FareFlightCacheManager;
import sg.com.wego.exception.FareFlightException;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.validator.FareFlightSearchValidatorImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FareFlightSearchServiceTest {

    @Mock
    private FareFlightCacheManager scheduleCacheManager;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Mock
    FareFlightSearchValidatorImpl fareFlightSearchValidator;

    @InjectMocks
    private FareFlightSearchServiceImpl metaSearchService;

    @Test
    public void shouldThrowFareFlightException_When_OneOfValidationViolate() {
        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        when(fareFlightSearchValidator.isDepartureAndArriValNotSame(any(MetaSearchRequest.class))).thenReturn(false);
        when(fareFlightSearchValidator.isDepartureAndArrivalCodeSupported(any(MetaSearchRequest.class))).thenReturn(true);
        when(fareFlightSearchValidator.isDepartureAndArrivalExistsOnSchudules(any(MetaSearchRequest.class))).thenReturn(false);

        assertThrows(FareFlightException.class, () -> metaSearchService.validateMetaSearchRequest(metaSearchCriteria));
    }

    @Test
    public void shouldByPassValidation_When_AllValidationValid() {
        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        when(fareFlightSearchValidator.isDepartureAndArriValNotSame(any(MetaSearchRequest.class))).thenReturn(true);
        when(fareFlightSearchValidator.isDepartureAndArrivalCodeSupported(any(MetaSearchRequest.class))).thenReturn(true);
        when(fareFlightSearchValidator.isDepartureAndArrivalExistsOnSchudules(any(MetaSearchRequest.class))).thenReturn(true);

        MetaSearchRequest expected = metaSearchService.validateMetaSearchRequest(metaSearchCriteria);

        assertNotNull(expected);

    }

}
