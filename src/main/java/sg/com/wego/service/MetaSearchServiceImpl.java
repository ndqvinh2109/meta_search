package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.model.ScheduleResponse;
import sg.com.wego.util.ValidatorHelper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static sg.com.wego.constant.MessageBundle.*;
import static sg.com.wego.mapper.FareFlightMapper.forMapper;

@Service
public class MetaSearchServiceImpl implements MetaSearchService {

    private static final Logger logger = LogManager.getLogger(MetaSearchServiceImpl.class);

    public static final int ALL = -1;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleCacheManager scheduleCacheManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MetaSearchValidator metaSearchValidator;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(30);
    }

    @Override
    public MetasearchResponse findFlight(MetaSearchRequest metaSearchRequest) {
        String generatedId = UUID.randomUUID().toString();
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchRequest.getDepartureCode(), metaSearchRequest.getArrivalCode());
        Map<String, List<Schedule>> scheduleMap = schedules.stream().collect(Collectors.groupingBy(Schedule::getProviderCode));

        simulateHttpRequestForCachingFareFlight(scheduleMap, generatedId);

        MetasearchResponse metasearchResponse = new MetasearchResponse();
        metasearchResponse.setGeneratedId(generatedId);
        return metasearchResponse;
    }

    @Override
    public MetaSearchRequest validate(MetaSearchRequest metaSearchRequest) {
        return ValidatorHelper.of(metaSearchRequest).
                validate(metaSearchValidator::isDepartureAndArriValNotSame, DEPARTURE_CODE_AND_ARRIVAL_CODE_MUST_NOT_BE_THE_SAME).
                validate(metaSearchValidator::isDepartureAndArrivalCodeSupported, UNSUPPORTED_DEPARTURE_CODE_OR_ARRIVAL_CODE).
                validate(metaSearchValidator::isDepartureAndArrivalExistsOnSchudules, THERE_WERE_NO_FLIGHT_EXISTING).
                get();
    }

    @Override
    public MetasearchResponse pollingFlight(String generatedId, long offset) {
        MetasearchResponse metasearchResponse = toMetasearchResponse(generatedId, offset);
        return metasearchResponse;
    }

    public List<FareFlight> cachingFareFlight(List<Schedule> schedules, String generatedId) throws InterruptedException {
        Thread.sleep(10000);
        List<FareFlight> fareFlights = schedules.stream().map(this::mapFareFlight).collect(Collectors.toList());
        fareFlights.forEach(fareFlight -> scheduleCacheManager.cacheSchedule(generatedId, fareFlight));
        return fareFlights;
    }

    private void simulateHttpRequestForCachingFareFlight(Map<String, List<Schedule>> scheduleMap, String generatedId) {
        scheduleMap.forEach((providerCode, scheduleList) -> executorService.execute(() -> {
            try {
                cachingFareFlight(scheduleList, generatedId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private MetasearchResponse toMetasearchResponse(String generatedId, long offset) {
        long size = scheduleCacheManager.getSize(generatedId);
        List<FareFlight> fareFlights = scheduleCacheManager.getCachedSchedules(generatedId, offset, ALL);
        MetasearchResponse metasearchResponse = new MetasearchResponse();
        metasearchResponse.setOffset(size);
        metasearchResponse.setGeneratedId(generatedId);
        metasearchResponse.setScheduleResponses(fareFlights.stream().map(this::mapScheduleResponse).collect(Collectors.toList()));
        return metasearchResponse;
    }

    private ScheduleResponse mapScheduleResponse(FareFlight fareFlight) {
        return forMapper(modelMapper).to(fareFlight);
    }

    private FareFlight mapFareFlight(Schedule schedule) {
        return forMapper(modelMapper).from(schedule);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

}
