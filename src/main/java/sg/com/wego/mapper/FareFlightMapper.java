package sg.com.wego.mapper;

import org.modelmapper.ModelMapper;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.ScheduleDto;

public class FareFlightMapper {

    private ModelMapper modelMapper;

    private FareFlightMapper(){}

    private FareFlightMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static FareFlightMapper forMapper (ModelMapper modelMapper) {
        return new FareFlightMapper(modelMapper);
    }

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
