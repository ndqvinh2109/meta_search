package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ProviderRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.mapper.FairFlightMapper;
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

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private ScheduleCacheManager scheduleCacheManager;

    @Autowired
    private FairFlightMapper fairFlightMapper;

    private ExecutorService executorService;

    private static final Logger logger = LogManager.getLogger(MetaSearchServiceImpl.class);

    @PostConstruct
    private void init() {
        executorService = Executors.newFixedThreadPool(30);
    }



    @Override
    public void findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) {
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode());

        for (Schedule schedule : schedules) {
            executorService.submit(() -> {
                try {
                    findSchedulesByProvider(schedules, schedule.getProviderCode(), generatedId);
                } catch (InterruptedException e) {
                    logger.error("An error occurred when getting fare from provider", e);
                }
            });

        }
    }

    private void findSchedulesByProvider(List<Schedule> schedules, String providerCode, String generatedId) throws InterruptedException {
        Thread.sleep(1000);
        schedules.stream().
                filter(schedule -> schedule.getProviderCode().equalsIgnoreCase(providerCode)).
                map(schedule -> fairFlightMapper.from(schedule)).
                forEach(fairFlight -> scheduleCacheManager.cacheSchedule(generatedId, fairFlight));
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
                                            map(fairFlight -> fairFlightMapper.to(fairFlight)).
                                            collect(Collectors.toList());

        metasearchDto.setScheduleDtoList(scheduleDtos);
        return metasearchDto;
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdown();
    }


}
