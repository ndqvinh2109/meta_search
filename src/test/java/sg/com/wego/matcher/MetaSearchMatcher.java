package sg.com.wego.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import sg.com.wego.model.ScheduleResponse;

public class MetaSearchMatcher extends BaseMatcher<ScheduleResponse> {

    private final ScheduleResponse scheduleDto;

    public MetaSearchMatcher(final ScheduleResponse scheduleDto) {
        this.scheduleDto = scheduleDto;
    }

    public static MetaSearchMatcher matchesScheduleDto(final ScheduleResponse scheduleDto) {
        return new MetaSearchMatcher(scheduleDto);
    }

    @Override
    public boolean matches(Object o) {
        ScheduleResponse schedule = (ScheduleResponse) o;
        return scheduleDto.getProviderCode().equalsIgnoreCase(schedule.getProviderCode()) &&
                scheduleDto.getArrivalAirportCode().equalsIgnoreCase(schedule.getArrivalAirportCode()) &&
                scheduleDto.getProviderCode().equalsIgnoreCase(schedule.getProviderCode());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(scheduleDto.getDepartAirportCode() +" "+ scheduleDto.getArrivalAirportCode() +" "+ scheduleDto.getProviderCode());
    }
}
