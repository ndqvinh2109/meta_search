package sg.com.wego.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.ScheduleDto;

@Component
public class FareFlightMapper {

    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    public ScheduleDto to (FareFlight fareFlight) {
        ScheduleDto scheduleDto = modelMapper.map(fareFlight, ScheduleDto.class);
        scheduleDto.setBasePrice(fareFlight.getBasePrice() + Math.random() * 0.3 * fareFlight.getBasePrice());
        return scheduleDto;
    }

    public FareFlight from (Schedule schedule) {
        FareFlight fareFlight = modelMapper.map(schedule, FareFlight.class);
        return fareFlight;
    }
}
