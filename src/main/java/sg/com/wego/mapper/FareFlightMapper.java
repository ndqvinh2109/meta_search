package sg.com.wego.mapper;

import org.modelmapper.ModelMapper;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.ScheduleResponse;

public class FareFlightMapper {

    private ModelMapper modelMapper;

    private FareFlightMapper(){}

    private FareFlightMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static FareFlightMapper forMapper (ModelMapper modelMapper) {
        return new FareFlightMapper(modelMapper);
    }

    public ScheduleResponse to (FareFlight fareFlight) {
        ScheduleResponse scheduleResponse = modelMapper.map(fareFlight, ScheduleResponse.class);
        return scheduleResponse;
    }

    public FareFlight from (Schedule schedule) {
        FareFlight fareFlight = modelMapper.map(schedule, FareFlight.class);
        fareFlight.setBasePrice(schedule.getBasePrice() + Math.random() * 0.3 * schedule.getBasePrice());
        return fareFlight;
    }
}
