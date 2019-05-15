package sg.com.wego.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.exception.FareFlightException;
import sg.com.wego.matcher.MetaSearchMatcher;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.model.ScheduleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetaSearchServiceTest {

    @Mock
    private ScheduleCacheManager scheduleCacheManager;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Mock
    MetaSearchValidatorImpl metaSearchValidator;

    @InjectMocks
    private MetaSearchServiceImpl metaSearchService;

    @Test
    public void shouldCacheFareFlight_When_ProviderCodeFound_In_Schedules() throws InterruptedException {
        metaSearchService.cachingFareFlight(prepareListSchedules(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(3)).cacheSchedule(anyString(), any(FareFlight.class));
    }

   @Test
    public void shouldNotCacheFareFlight_WhenProvider_NotFound_In_Schedules() throws InterruptedException {
        metaSearchService.cachingFareFlight(new ArrayList<>(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
       verify(scheduleCacheManager, times(0)).cacheSchedule(anyString(), any(FareFlight.class));
    }

    @Test
    public void shouldTransform_MetaSearchToClient_When_Data_Exists_In_Redis() {

        when(scheduleCacheManager.getSize("0a946908-cfc0-449a-b7aa-bb18ab4cea7a")).thenReturn(1L);
        when(scheduleCacheManager.getCachedSchedules("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0, -1)).thenReturn(prepareListFareFlight());

        MetasearchResponse metasearchResponse = metaSearchService.pollingFlight("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0);

        ScheduleResponse actual = new ScheduleResponse();
        actual.setDepartAirportCode("AAR");
        actual.setArrivalAirportCode("ADB");
        actual.setProviderCode("jetstart.com");

        ScheduleResponse expected = metasearchResponse.getScheduleResponses().get(0);

        assertThat(metasearchResponse.getOffset(), Matchers.is(1L));
        assertThat(metasearchResponse.getScheduleResponses().size(), Matchers.is(1));
        assertThat(expected, MetaSearchMatcher.matchesScheduleDto(actual));

    }

    @Test
    public void shouldResponseEmpty_ToClient_When_Data_NotExists_In_Redis() {

        when(scheduleCacheManager.getSize("0a946908-cfc0-449a-b7aa-bb18ab4cea7a")).thenReturn(0L);
        when(scheduleCacheManager.getCachedSchedules("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0, -1)).thenReturn(new ArrayList<>());

        MetasearchResponse metasearchDto = metaSearchService.pollingFlight("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0);
        assertThat(metasearchDto.getOffset(), Matchers.is(0L));
        assertThat(metasearchDto.getScheduleResponses().size(), Matchers.is(0));
    }

    @Test
    public void shouldThrowFareFlightException_When_OneOfValidationViolate() {
        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        when(metaSearchValidator.isDepartureAndArriValNotSame(any(MetaSearchRequest.class))).thenReturn(false);
        when(metaSearchValidator.isDepartureAndArrivalCodeSupported(any(MetaSearchRequest.class))).thenReturn(true);
        when(metaSearchValidator.isDepartureAndArrivalExistsOnSchudules(any(MetaSearchRequest.class))).thenReturn(false);

        assertThrows(FareFlightException.class, () -> metaSearchService.validate(metaSearchCriteria));
    }

    @Test
    public void shouldByPassValidation_When_AllValidationValid() {
        MetaSearchRequest metaSearchCriteria = new MetaSearchRequest();
        metaSearchCriteria.setDepartureCode("AAR");
        metaSearchCriteria.setArrivalCode("AAR");

        when(metaSearchValidator.isDepartureAndArriValNotSame(any(MetaSearchRequest.class))).thenReturn(true);
        when(metaSearchValidator.isDepartureAndArrivalCodeSupported(any(MetaSearchRequest.class))).thenReturn(true);
        when(metaSearchValidator.isDepartureAndArrivalExistsOnSchudules(any(MetaSearchRequest.class))).thenReturn(true);

        MetaSearchRequest expected = metaSearchService.validate(metaSearchCriteria);

        assertNotNull(expected);

    }

    private List<FareFlight> prepareListFareFlight() {
        FareFlight fareFlight = new FareFlight();
        fareFlight.setDepartAirportCode("AAR");
        fareFlight.setArrivalAirportCode("ADB");
        fareFlight.setProviderCode("jetstart.com");
        fareFlight.setBasePrice(553D);


        List<FareFlight> fareFlights = new ArrayList<>();
        fareFlights.add(fareFlight);
        return fareFlights;
    }

    private List<Schedule> prepareListSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        schedule.setDepartAirportCode("AAR");
        schedule.setArrivalAirportCode("ADB");
        schedule.setProviderCode("jetstart.com");
        schedule.setBasePrice(553);

        schedules.add(schedule);

        schedule = new Schedule();
        schedule.setDepartAirportCode("AAR");
        schedule.setArrivalAirportCode("ADB");
        schedule.setProviderCode("vnairline.com");
        schedule.setBasePrice(415);

        schedules.add(schedule);

        schedule = new Schedule();
        schedule.setDepartAirportCode("AAR");
        schedule.setArrivalAirportCode("ADB");
        schedule.setProviderCode("vietjetair.com");
        schedule.setBasePrice(312);

        schedules.add(schedule);

        return schedules;
    }
}
