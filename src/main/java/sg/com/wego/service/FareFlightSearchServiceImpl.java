package sg.com.wego.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.service.searchable.SearchEngine;
import sg.com.wego.validator.FareFlightSearchValidator;
import sg.com.wego.validator.ValidatorHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static sg.com.wego.constant.MessageBundle.*;

@Service
public class FareFlightSearchServiceImpl implements FareFlightSearchService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FareFlightSearchValidator fareFlightSearchValidator;

    @Autowired
    private SearchEngine flightSearchEngineBean;


    @Override
    public MetasearchResponse findFlight(MetaSearchRequest metaSearchRequest) {
        String generatedId = UUID.randomUUID().toString();
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchRequest.getDepartureCode(), metaSearchRequest.getArrivalCode());
        Map<String, List<Schedule>> scheduleMap = schedules.stream().collect(Collectors.groupingBy(Schedule::getProviderCode));

        flightSearchEngineBean.simulateHttpRequestForCachingFareFlight(scheduleMap, generatedId);

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
        return flightSearchEngineBean.pollingFlight(generatedId, offset);
    }

}
