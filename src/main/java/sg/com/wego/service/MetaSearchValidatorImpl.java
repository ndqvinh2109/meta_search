package sg.com.wego.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ScheduleRepository;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class MetaSearchValidatorImpl implements MetaSearchValidator<MetaSearchCriteria>{

    private static final Logger logger = LogManager.getLogger(MetaSearchValidatorImpl.class);

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public boolean isDepartureAndArrivalCodeSupported(MetaSearchCriteria metaSearchCriteria) {
         return isNotEmpty(airportRepository.findAllByCode(metaSearchCriteria.getDepartureCode())) &&
                 isNotEmpty(airportRepository.findAllByCode(metaSearchCriteria.getArrivalCode()));
    }

    @Override
    public boolean isDepartureAndArriValNotSame(MetaSearchCriteria metaSearchCriteria) {
        return !metaSearchCriteria.getDepartureCode().equalsIgnoreCase(metaSearchCriteria.getArrivalCode());
    }

    @Override
    public boolean isDepartureAndArrivalExistsOnSchudules(MetaSearchCriteria metaSearchCriteria) {
        return isNotEmpty(scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode()));
    }

}
