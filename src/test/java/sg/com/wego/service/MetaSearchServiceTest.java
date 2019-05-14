package sg.com.wego.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.mapper.FareFlightMapper;
import sg.com.wego.model.MetasearchDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetaSearchServiceTest {

    @Mock
    private ScheduleCacheManager scheduleCacheManager;

    @Spy
    private FareFlightMapper fareFlightMapper;

    @InjectMocks
    private MetaSearchServiceImpl metaSearchService;


    @Test
    public void shouldCacheFareFlight_IF_ProviderCodeFound_In_Schedules() {
        List<FareFlight> fareFlights = metaSearchService.findSchedulesFromProviderCode(prepareListSchedules(), "jetstart.com", "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");

        Assert.assertThat(fareFlights.size(), Matchers.is(1));
        verify(scheduleCacheManager, times(1)).cacheSchedule("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", fareFlights.get(0));
    }

    @Test
    public void shouldNotCacheFareFlight_IFProvider_NotFound_In_Schedules() {
        metaSearchService.findSchedulesFromProviderCode(prepareListSchedules(), "Dubai.com", "0a946908-cfc0-449a-b7aa-bb18ab4cea7a");
        verify(scheduleCacheManager, times(0)).cacheSchedule(anyString(), any(FareFlight.class));
    }


    @Test
    public void shouldTransform_MetaSearchToClient_IF_Data_Exists_In_Redis() {

        when(scheduleCacheManager.getSize("0a946908-cfc0-449a-b7aa-bb18ab4cea7a")).thenReturn(1L);
        when(scheduleCacheManager.getCachedSchedules("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0, -1)).thenReturn(prepareListFareFlight());


        MetasearchDto metasearchDto = metaSearchService.pollingFlight("0a946908-cfc0-449a-b7aa-bb18ab4cea7a", 0);
        Assert.assertThat(metasearchDto.getOffset(), Matchers.is(1L));
        Assert.assertThat(metasearchDto.getScheduleDtoList().size(), Matchers.is(1));
        Assert.assertThat(metasearchDto.getScheduleDtoList().get(0).getProviderCode(), Matchers.is("jetstart.com"));
        Assert.assertThat(metasearchDto.getScheduleDtoList().get(0).getDepartAirportCode(), Matchers.is("AAR"));
        Assert.assertThat(metasearchDto.getScheduleDtoList().get(0).getArrivalAirportCode(), Matchers.is("ADB"));
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
