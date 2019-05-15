package sg.com.wego.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.util.RedisUtil;

import java.util.List;

@Service
public class ScheduleCacheManagerImpl implements ScheduleCacheManager {


    @Autowired
    private RedisUtil<FareFlight> scheduleRedisUtil;

    @Override
    public void cacheSchedule(String generatedId, FareFlight fareFlight) {
       scheduleRedisUtil.putList(generatedId, fareFlight);
    }

    @Override
    public List<FareFlight> getCachedSchedules(String generatedId, long i, long j) {
        List<FareFlight> fareFlights = scheduleRedisUtil.getListRange(generatedId, i, j);
        return fareFlights;
    }

    @Override
    public long getSize(String generatedId) {
        return scheduleRedisUtil.getListSize(generatedId);
    }



}
