package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ProviderRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.dto.MetasearchDto;
import sg.com.wego.dto.ScheduleDto;
import sg.com.wego.entity.Schedule;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private ScheduleCacheManager scheduleCacheManager;

    private ExecutorService executorService;

    private static final Logger logger = LogManager.getLogger(MetaSearchServiceImpl.class);

    @PostConstruct
    private void init() {
        executorService = Executors.newFixedThreadPool(30);
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdown();
    }

    @Override
    public void findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) throws InterruptedException, ExecutionException {
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode());

        for (Schedule schedule : schedules) {
            executorService.submit(() -> callRestToGetSchedules(metaSearchCriteria, schedule.getProviderCode(), generatedId));
        }

    }

    @Override
    public MetasearchDto pollingFlight(String generatedId, long offset) {
        long size = scheduleCacheManager.getSize(generatedId);
        MetasearchDto metasearchDto = new MetasearchDto();
        metasearchDto.setOffset(size);
        metasearchDto.setScheduleDtoList(scheduleCacheManager.getCachedSchedules(generatedId, offset, -1));
        return metasearchDto;
    }

    private List<ScheduleDto> callRestToGetSchedules(MetaSearchCriteria metaSearchCriteria, String providerCode, String generatedId) throws InterruptedException {
        String url = "http://localhost:8080/api/schedules";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("departureCode", metaSearchCriteria.getDepartureCode())
                .queryParam("arrivalCode", metaSearchCriteria.getArrivalCode())
                .queryParam("providerCode", providerCode);

        logger.info("Calling rest api: " + builder.toUriString());

        ResponseEntity<List<ScheduleDto>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ScheduleDto>>() {
        });

        Thread.sleep(10000);

        List<ScheduleDto> schedules = response.getBody();

        schedules.forEach(scheduleDto -> scheduleCacheManager.cacheSchedule(generatedId, scheduleDto));

        return schedules;
    }


}
