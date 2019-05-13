package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ProviderRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.mapper.FareFlightMapper;
import sg.com.wego.model.MetasearchDto;
import sg.com.wego.model.ScheduleDto;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class MetaSearchServiceImpl implements MetaSearchService {

    private static final Logger logger = LogManager.getLogger(MetaSearchServiceImpl.class);

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleCacheManager scheduleCacheManager;

    @Autowired
    private FareFlightMapper fareFlightMapper;

    private ExecutorService executorService;

    @PostConstruct
    private void init() {
        executorService = Executors.newFixedThreadPool(30);
    }


    @Override
    public void findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) {
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode());

        for (Schedule schedule : schedules) {
            executorService.submit(() -> findSchedulesFromProviderCode(schedules, schedule.getProviderCode(), generatedId));
        }
    }

    public void findSchedulesFromProviderCode(List<Schedule> schedules, String providerCode, String generatedId) {
        List<FareFlight> fareFlights = schedules.stream().filter(schedule -> schedule.getProviderCode().equalsIgnoreCase(providerCode)).
                map(this::mapFareFlight).collect(Collectors.toList());
        fareFlights.forEach(fareFlight -> scheduleCacheManager.cacheSchedule(generatedId, fareFlight));
    }

    private FareFlight mapFareFlight(Schedule schedule) {
        return fareFlightMapper.from(schedule);
    }

    @Override
    public MetasearchDto pollingFlight(String generatedId, long offset) {
        MetasearchDto metasearchDto = toMetasearchDto(generatedId, offset);
        return metasearchDto;
    }

    private MetasearchDto toMetasearchDto(String generatedId, long offset) {
        long size = scheduleCacheManager.getSize(generatedId);
        MetasearchDto metasearchDto = new MetasearchDto();
        metasearchDto.setOffset(size);
        List<ScheduleDto> scheduleDtos = scheduleCacheManager.getCachedSchedules(generatedId, offset, -1).stream().
                map(fareFlight -> fareFlightMapper.to(fareFlight)).
                collect(Collectors.toList());
        metasearchDto.setScheduleDtoList(scheduleDtos);
        return metasearchDto;
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdown();
    }

}
