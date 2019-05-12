package sg.com.wego.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import sg.com.wego.dto.ScheduleDto;
import sg.com.wego.util.RedisUtil;

import java.util.List;

@Configuration
public class ScheduleCacheManagerImpl implements ScheduleCacheManager {


    @Autowired
    private RedisUtil<ScheduleDto> scheduleRedisUtil;

    @Override
    public void cacheSchedule(String generatedId, ScheduleDto scheduleDto) {
       scheduleRedisUtil.putList(generatedId, scheduleDto);
    }

    @Override
    public List<ScheduleDto> getCachedSchedules(String generatedId, long i, long j) {
        List<ScheduleDto>scheduleDtos = scheduleRedisUtil.getListRange(generatedId, i, j);
        return scheduleDtos;
    }

    @Override
    public long getSize(String generatedId) {
        return scheduleRedisUtil.getListSize(generatedId);
    }



}
