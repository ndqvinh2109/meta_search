package sg.com.wego.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.mapper.FareFlightMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetaSearchServiceTest {

    @Mock
    private ScheduleCacheManager scheduleCacheManager;

    @Mock
    private FareFlightMapper fareFlightMapper;

    @InjectMocks
    private MetaSearchServiceImpl metaSearchService;


    @Test
    public void shouldCache_FareFlight_IF_FoundProviderCode_In_Schedules() {
        FareFlight fareFlight = new FareFlight();
        fareFlight.setDepartAirportCode("AAR");
        fareFlight.setArrivalAirportCode("ADB");
        fareFlight.setProviderCode("jetstart.com");
        fareFlight.setBasePrice(553D);


        when(fareFlightMapper.from(any(Schedule.class))).thenReturn(fareFlight);
        metaSearchService.findSchedulesFromProviderCode(prepareListSchedules(), "jetstart.com", "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");

        verify(scheduleCacheManager, times(1)).cacheSchedule("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", fareFlight);
    }

    @Test
    public void shouldNotCache_FareFlight_IF_FoundProviderCode_In_Schedules() {
        metaSearchService.findSchedulesFromProviderCode(prepareListSchedules(), "Dubai.com", "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(0)).cacheSchedule(anyString(), any(FareFlight.class));
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
