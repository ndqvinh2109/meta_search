package sg.com.wego.cache;

import sg.com.wego.cache.entity.FairFlight;

import java.util.List;

public interface ScheduleCacheManager {
    void cacheSchedule(String generateId, FairFlight fairFlight);
    List<FairFlight> getCachedSchedules(String generatedId, long i, long j);
    long getSize(String generateId);

}
