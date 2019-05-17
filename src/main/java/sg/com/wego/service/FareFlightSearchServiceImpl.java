package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.FareFlightCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.model.ScheduleResponse;
import sg.com.wego.validator.FareFlightSearchValidator;
import sg.com.wego.validator.ValidatorHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static sg.com.wego.constant.MessageBundle.*;
import static sg.com.wego.mapper.FareFlightMapper.forMapper;

@Service
public class FareFlightSearchServiceImpl implements FareFlightSearchService {

    private static final Logger logger = LogManager.getLogger(FareFlightSearchServiceImpl.class);

    public static final int ALL = -1;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FareFlightCacheManager scheduleCacheManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FareFlightSearchValidator fareFlightSearchValidator;

    @Autowired
    private SearchEngine searchEngine;


    @Override
    public MetasearchResponse findFlight(MetaSearchRequest metaSearchRequest) {
        String generatedId = UUID.randomUUID().toString();
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchRequest.getDepartureCode(), metaSearchRequest.getArrivalCode());
        Map<String, List<Schedule>> scheduleMap = schedules.stream().collect(Collectors.groupingBy(Schedule::getProviderCode));

        searchEngine.simulateHttpRequestForCachingFareFlight(scheduleMap, generatedId);

        MetasearchResponse metasearchResponse = new MetasearchResponse();
        metasearchResponse.setGeneratedId(generatedId);
        return metasearchResponse;
    }


    @Override
    public MetaSearchRequest validateMetaSearchRequest(MetaSearchRequest metaSearchRequest) {
        return ValidatorHelper.of(metaSearchRequest).
                validate(fareFlightSearchValidator::isDepartureAndArriValNotSame, DEPARTURE_CODE_AND_ARRIVAL_CODE_MUST_NOT_BE_THE_SAME).
                validate(fareFlightSearchValidator::isDepartureAndArrivalCodeSupported, UNSUPPORTED_DEPARTURE_CODE_OR_ARRIVAL_CODE).
                validate(fareFlightSearchValidator::isDepartureAndArrivalExistsOnSchudules, THERE_WERE_NO_FLIGHT_EXISTING).
                get();
    }

    @Override
    public MetasearchResponse pollingFlight(String generatedId, long offset) {
        MetasearchResponse metasearchResponse = toMetasearchResponse(generatedId, offset);
        return metasearchResponse;
    }


    private MetasearchResponse toMetasearchResponse(String generatedId, long offset) {
        long size = scheduleCacheManager.getSizeOfFareFlights(generatedId);
        List<FareFlight> fareFlights = scheduleCacheManager.findFareFlightByGenerateId(generatedId, offset, ALL);
        MetasearchResponse metasearchResponse = new MetasearchResponse();
        metasearchResponse.setOffset(size);
        metasearchResponse.setGeneratedId(generatedId);
        metasearchResponse.setScheduleResponses(fareFlights.stream().map(this::mapScheduleResponse).collect(Collectors.toList()));
        return metasearchResponse;
    }

    private ScheduleResponse mapScheduleResponse(FareFlight fareFlight) {
        return forMapper(modelMapper).to(fareFlight);
    }

}
