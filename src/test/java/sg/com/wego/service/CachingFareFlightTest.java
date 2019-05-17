package sg.com.wego.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CachingFareFlightTest {
    @Mock
    private FareFlightCacheManager scheduleCacheManager;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @InjectMocks
    private FlightSearchEngine metaSearchService;

    @Test
    public void shouldCacheFareFlight_When_ProviderCodeFound_In_Schedules() throws InterruptedException {
        metaSearchService.cachingFareFlight(prepareListSchedules(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(3)).cacheFareFlight(anyString(), any(FareFlight.class));
    }

    @Test
    public void shouldNotCacheFareFlight_WhenProvider_NotFound_In_Schedules() throws InterruptedException {
        metaSearchService.cachingFareFlight(new ArrayList<>(), "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(0)).cacheFareFlight(anyString(), any(FareFlight.class));
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
