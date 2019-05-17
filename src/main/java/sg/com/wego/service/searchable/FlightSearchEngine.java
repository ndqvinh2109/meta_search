package sg.com.wego.service.searchable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.FareFlightCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.FareFlightSearchParam;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.model.ScheduleResponse;
import sg.com.wego.service.sortable.FareFlightComparator;
import sg.com.wego.service.sortable.FareFlightComparatorFactory;
import sg.com.wego.util.SortingUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.com.wego.mapper.FareFlightMapper.forMapper;
import static sg.com.wego.util.SortingUtil.comparatorOf;

@Service
public class FlightSearchEngine implements SearchEngine<Schedule>  {

    private static final Logger logger = LogManager.getLogger(FlightSearchEngine.class);

    public static final int ALL = -1;

    @Autowired
    private FareFlightCacheManager scheduleCacheManager;

    @Autowired
    private ModelMapper modelMapper;

    private ExecutorService executorService;

    @PostConstruct
    public void initExecutor() {
        executorService = Executors.newFixedThreadPool(30);
    }

    @Override
    public void simulateHttpRequestForCachingFareFlight(Map<String, List<Schedule>> scheduleMap, String generatedId) {
        scheduleMap.forEach((providerCode, scheduleList) -> executorService.execute(() -> {
            try {
                cachingFareFlight(scheduleList, generatedId);
            } catch (InterruptedException e) {
                logger.error("An error occured when caching fare flight " + e);
            }
        }));
    }

    @Override
    public MetasearchResponse pollingFlight(String generatedId, long offset) {
        MetasearchResponse metasearchResponse = toMetasearchResponse(generatedId, offset);
        return metasearchResponse;
    }

    private MetasearchResponse toMetasearchResponse(String generatedId, long offset) {
        long size = scheduleCacheManager.getSizeOfFareFlights(generatedId);
        List<FareFlight> fareFlights = scheduleCacheManager.findFareFlightByGenerateId(generatedId, offset, ALL);

        sortByCheapestFareFlights(fareFlights);

        MetasearchResponse metasearchResponse = new MetasearchResponse();
        metasearchResponse.setOffset(size);
        metasearchResponse.setGeneratedId(generatedId);
        metasearchResponse.setScheduleResponses(fareFlights.stream().map(this::mapScheduleResponse).collect(Collectors.toList()));
        return metasearchResponse;
    }

    private void sortByCheapestFareFlights(List<FareFlight> fareFlights) {
        Comparator<FareFlight> fareFlightComparator = comparatorOf (
                getFareFlightSearchField(FareFlightSearchParam.PRICE, FareFlightComparatorFactory::getFareFlightComparator),
                SortingUtil.Order.ASCENDING,
                SortingUtil.Nulls.LAST);

        Collections.sort(fareFlights, fareFlightComparator);
    }

    private ScheduleResponse mapScheduleResponse(FareFlight fareFlight) {
        return forMapper(modelMapper).to(fareFlight);
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

    private static <T, R extends Comparable> Function<T, R> getFareFlightSearchField (FareFlightSearchParam parameters, FareFlightComparator<T, R> fareFlightComparator) {
        return fareFlightComparator.getValue(parameters);
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
    }

}
