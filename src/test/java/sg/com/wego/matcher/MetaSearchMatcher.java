package sg.com.wego.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import sg.com.wego.model.ScheduleDto;

public class MetaSearchMatcher extends BaseMatcher<ScheduleDto> {

    private final ScheduleDto scheduleDto;

    public MetaSearchMatcher(final ScheduleDto scheduleDto) {
        this.scheduleDto = scheduleDto;
    }

    public static MetaSearchMatcher matchesScheduleDto(final ScheduleDto scheduleDto) {
        return new MetaSearchMatcher(scheduleDto);
    }

    @Override
    public boolean matches(Object o) {
        ScheduleDto schedule = (ScheduleDto) o;
        return scheduleDto.getProviderCode().equalsIgnoreCase(schedule.getProviderCode()) &&
                scheduleDto.getArrivalAirportCode().equalsIgnoreCase(schedule.getArrivalAirportCode()) &&
                scheduleDto.getProviderCode().equalsIgnoreCase(schedule.getProviderCode());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(scheduleDto.getDepartAirportCode() +" "+ scheduleDto.getArrivalAirportCode() +" "+ scheduleDto.getProviderCode());
    }
}
