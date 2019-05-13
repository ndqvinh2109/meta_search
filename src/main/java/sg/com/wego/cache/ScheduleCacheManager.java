package sg.com.wego.cache;

import sg.com.wego.cache.entity.FareFlight;

import java.util.List;

public interface ScheduleCacheManager {
    void cacheSchedule(String generateId, FareFlight fareFlight);
    List<FareFlight> getCachedSchedules(String generatedId, long i, long j);
    long getSize(String generateId);

}
