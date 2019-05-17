package sg.com.wego.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.model.MetaSearchRequest;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class FareFlightSearchValidatorImpl implements FareFlightSearchValidator<MetaSearchRequest> {

    private static final Logger logger = LogManager.getLogger(FareFlightSearchValidatorImpl.class);

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public boolean isDepartureAndArrivalCodeSupported(MetaSearchRequest metaSearchCriteria) {
         return isNotEmpty(airportRepository.findAllByCode(metaSearchCriteria.getDepartureCode())) &&
                 isNotEmpty(airportRepository.findAllByCode(metaSearchCriteria.getArrivalCode()));
    }

    @Override
    public boolean isDepartureAndArriValNotSame(MetaSearchRequest metaSearchCriteria) {
        return !metaSearchCriteria.getDepartureCode().equalsIgnoreCase(metaSearchCriteria.getArrivalCode());
    }

    @Override
    public boolean isDepartureAndArrivalExistsOnSchudules(MetaSearchRequest metaSearchCriteria) {
        return isNotEmpty(scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(metaSearchCriteria.getDepartureCode(), metaSearchCriteria.getArrivalCode()));
    }

}
