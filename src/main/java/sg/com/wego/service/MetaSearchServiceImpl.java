package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.ScheduleCacheManager;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.MetasearchDto;
import sg.com.wego.model.ScheduleDto;
import sg.com.wego.util.Validator;

import java.util.List;
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


    @Override
    @Async("asyncExecutor")
    public List<FareFlight> findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) throws InterruptedException {
        List<FareFlight> fareFlights = findSchedulesFromProviderCode(metaSearchCriteria);
        cachingFareFlight(fareFlights, generatedId);
        return fareFlights;
    }

    @Override
    public MetaSearchCriteria validate(MetaSearchCriteria metaSearchCriteria) {
        return Validator.of(metaSearchCriteria).
                validate(metaSearchValidator::isDepartureAndArriValNotSame, DEPARTURE_CODE_AND_ARRIVAL_CODE_MUST_NOT_BE_THE_SAME).
                validate(metaSearchValidator::isDepartureAndArrivalCodeSupported, UNSUPPORTED_DEPARTURE_CODE_OR_ARRIVAL_CODE).
                validate(metaSearchValidator::isDepartureAndArrivalExistsOnSchudules, THERE_WERE_NO_FLIGHT_EXISTING).
                get();
    }

    private void cachingFareFlight(List<FareFlight> fareFlights, String generatedId) {
        fareFlights.forEach(fareFlight -> scheduleCacheManager.cacheSchedule(generatedId, fareFlight));
    }

    private List<FareFlight> findSchedulesFromProviderCode(MetaSearchCriteria metaSearchCriteria) throws InterruptedException {
        Thread.sleep(10000);
        List<Schedule> schedules = scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode());
        return schedules.stream().map(this::mapFareFlight).collect(Collectors.toList());
    }

    private FareFlight mapFareFlight(Schedule schedule) {
        return forMapper(modelMapper).from(schedule);
    }

    @Override
    public MetasearchDto pollingFlight(String generatedId, long offset) {
        MetasearchDto metasearchDto = toMetasearchDto(generatedId, offset);
        return metasearchDto;
    }

    private MetasearchDto toMetasearchDto(String generatedId, long offset) {
        long size = scheduleCacheManager.getSize(generatedId);
        List<FareFlight> fareFlights = scheduleCacheManager.getCachedSchedules(generatedId, offset, ALL);
        MetasearchDto metasearchDto = new MetasearchDto();
        metasearchDto.setOffset(size);
        metasearchDto.setScheduleDtoList(fareFlights.stream().map(this::mapScheduleDto).collect(Collectors.toList()));
        return metasearchDto;
    }

    private ScheduleDto mapScheduleDto(FareFlight fareFlight) {
        return forMapper(modelMapper).to(fareFlight);
    }


}
