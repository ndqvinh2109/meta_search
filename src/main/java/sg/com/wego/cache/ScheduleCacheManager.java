package sg.com.wego.cache;

import sg.com.wego.dto.ScheduleDto;

import java.util.List;

public interface ScheduleCacheManager {
    void cacheSchedule(String generateId, ScheduleDto scheduleDto);
    List<ScheduleDto> getCachedSchedules(String generatedId, long i, long j);
    long getSize(String generateId);

}
