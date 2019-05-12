package sg.com.wego.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sg.com.wego.dto.ScheduleDto;
import sg.com.wego.entity.Schedule;

@Configuration
public class ScheduleMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public ScheduleDto from(Schedule schedule) {
        ScheduleDto scheduleDto = modelMapper.map(schedule, ScheduleDto.class);
        scheduleDto.setBasePrice(schedule.getBasePrice() + Math.random() * 0.3 * schedule.getBasePrice());
        return scheduleDto;
    }

    public Schedule to(ScheduleDto scheduleDto) {
        Schedule schedule = modelMapper.map(scheduleDto, Schedule.class);
        return schedule;
    }

}
