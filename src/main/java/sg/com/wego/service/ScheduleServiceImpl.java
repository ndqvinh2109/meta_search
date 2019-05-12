package sg.com.wego.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.dao.ScheduleRepository;
import sg.com.wego.entity.Schedule;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> findAllSchedulesByDepartAirportCodeAndArrivalAirportCodeAndProviderCode(String departureCode, String arrivalCode, String providerCode) {
        return scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCodeAndProviderCode(departureCode, arrivalCode, providerCode);
    }

    @Override
    public List<Schedule> findAllSchedulesByDepartAirportCodeAndArrivalAirportCode(String departCode, String arrivalCode) {
        return scheduleRepository.findAllByDepartAirportCodeAndArrivalAirportCode(departCode, arrivalCode);
    }



}
