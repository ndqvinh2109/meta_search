package sg.com.wego.service;

import sg.com.wego.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    List<Schedule> findAllSchedulesByDepartAirportCodeAndArrivalAirportCodeAndProviderCode(String departureCode, String arrivalCode, String providerCode);

    List<Schedule> findAllSchedulesByDepartAirportCodeAndArrivalAirportCode(String departCode, String arrivalCode);
}
