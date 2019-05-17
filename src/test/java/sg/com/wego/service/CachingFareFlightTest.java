package sg.com.wego.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import sg.com.wego.cache.FareFlightCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.matcher.MetaSearchMatcher;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.model.ScheduleResponse;
import sg.com.wego.service.searchable.FlightSearchEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CachingFareFlightTest {
    @Mock
    private FareFlightCacheManager scheduleCacheManager;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @InjectMocks
    private FlightSearchEngine flightSearchEngine;

    @Test
    public void shouldCacheFareFlight_When_ProviderCodeFound_In_Schedules() throws InterruptedException {
        flightSearchEngine.cachingFareFlight(prepareListSchedules(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(3)).cacheFareFlight(anyString(), any(FareFlight.class));
    }

    @Test
    public void shouldNotCacheFareFlight_WhenProvider_NotFound_In_Schedules() throws InterruptedException {
        flightSearchEngine.cachingFareFlight(new ArrayList<>(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(0)).cacheFareFlight(anyString(), any(FareFlight.class));
    }

    @Test
    public void shouldTransform_MetaSearchToClient_When_Data_Exists_In_Redis() {

        when(scheduleCacheManager.getSizeOfFareFlights("0a946908-cfc0-449a-b7aa-bb18ab4cea7a")).thenReturn(1L);
        when(scheduleCacheManager.findFareFlightByGenerateId("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0, -1)).thenReturn(prepareListFareFlight());

        MetasearchResponse metasearchResponse = flightSearchEngine.pollingFlight("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0);

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

        when(scheduleCacheManager.getSizeOfFareFlights("0a946908-cfc0-449a-b7aa-bb18ab4cea7a")).thenReturn(0L);
        when(scheduleCacheManager.findFareFlightByGenerateId("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0, -1)).thenReturn(new ArrayList<>());

        MetasearchResponse metasearchDto = flightSearchEngine.pollingFlight("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0);
        assertThat(metasearchDto.getOffset(), Matchers.is(0L));
        assertThat(metasearchDto.getScheduleResponses().size(), Matchers.is(0));
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
}
