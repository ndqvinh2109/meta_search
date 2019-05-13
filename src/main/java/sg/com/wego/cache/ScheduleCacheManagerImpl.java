package sg.com.wego.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import sg.com.wego.cache.entity.FairFlight;
import sg.com.wego.util.RedisUtil;

import java.util.List;

@Configuration
public class ScheduleCacheManagerImpl implements ScheduleCacheManager {


    @Autowired
    private RedisUtil<FairFlight> fairFlightRedisUtil;

    @Override
    public void cacheSchedule(String generatedId, FairFlight fairFlight) {
        fairFlightRedisUtil.putList(generatedId, fairFlight);
    }

    @Override
    public List<FairFlight> getCachedSchedules(String generatedId, long i, long j) {
        List<FairFlight> fairFlights = fairFlightRedisUtil.getListRange(generatedId, i, j);
        return fairFlights;
    }

    @Override
    public long getSize(String generatedId) {
        return fairFlightRedisUtil.getListSize(generatedId);
    }



}
