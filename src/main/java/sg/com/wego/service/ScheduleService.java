package sg.com.wego.service;

import sg.com.wego.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> findAllSchedulesByDepartAirportCodeAndArrivalAirportCode(String departCode, String arrivalCode);
}
