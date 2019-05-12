package sg.com.wego.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.com.wego.dto.ScheduleDto;
import sg.com.wego.entity.Schedule;
import sg.com.wego.mapper.ScheduleMapper;
import sg.com.wego.service.ScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FlightScheduleResource {
    private static final Logger logger = LogManager.getLogger(FlightScheduleResource.class);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @GetMapping("/schedules")
    public List<ScheduleDto> findSchedulesByCode(@RequestParam String departureCode, @RequestParam String arrivalCode, @RequestParam String providerCode) {
        List<Schedule> schedules = scheduleService.findAllSchedulesByDepartAirportCodeAndArrivalAirportCodeAndProviderCode(departureCode, arrivalCode, providerCode);
        return schedules.stream().map(schedule -> scheduleMapper.from(schedule)).collect(Collectors.toList());
    }
}
