package sg.com.wego.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.com.wego.cache.entity.FairFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.ScheduleDto;

@Component
public class FairFlightMapper {

    @Autowired
    private ModelMapper modelMapper;


    public ScheduleDto to(FairFlight fairFlight) {
        ScheduleDto scheduleDto = modelMapper.map(fairFlight, ScheduleDto.class);
        return scheduleDto;
    }

    public FairFlight from(Schedule schedule) {
        FairFlight fairFlight = modelMapper.map(schedule, FairFlight.class);
        return fairFlight;
    }

}
