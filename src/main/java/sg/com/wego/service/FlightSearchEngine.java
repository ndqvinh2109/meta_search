package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.FareFlightCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static sg.com.wego.mapper.FareFlightMapper.forMapper;

@Service
public class FlightSearchEngine implements SearchEngine<Schedule>  {

    private static final Logger logger = LogManager.getLogger(FlightSearchEngine.class);

    @Autowired
    private FareFlightCacheManager scheduleCacheManager;

    @Autowired
    private ModelMapper modelMapper;

    private ExecutorService executorService;




    @Override
    public void simulateHttpRequestForCachingFareFlight(Map<String, List<Schedule>> scheduleMap, String generatedId) {
        logger.info("scheduleMap" + scheduleMap);
        logger.info("executorService" + executorService);
        executorService = Executors.newFixedThreadPool(30);
        scheduleMap.forEach((providerCode, scheduleList) -> executorService.execute(() -> {
            try {
                cachingFareFlight(scheduleList, generatedId);
            } catch (InterruptedException e) {
                logger.error("An error occured when caching fare flight " + e);
            }
        }));
    }

    public List<FareFlight> cachingFareFlight(List<Schedule> schedules, String generatedId) throws InterruptedException {
        Thread.sleep(10000);
        List<FareFlight> fareFlights = schedules.stream().map(this::mapFareFlight).collect(Collectors.toList());
        fareFlights.forEach(fareFlight -> scheduleCacheManager.cacheFareFlight(generatedId, fareFlight));
        return fareFlights;
    }

    private FareFlight mapFareFlight(Schedule schedule) {
        return forMapper(modelMapper).from(schedule);
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
    }


}
