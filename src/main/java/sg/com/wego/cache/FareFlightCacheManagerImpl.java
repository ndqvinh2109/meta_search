package sg.com.wego.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.util.RedisUtil;

import java.util.List;

@Service
public class FareFlightCacheManagerImpl implements FareFlightCacheManager {


    @Autowired
    private RedisUtil<FareFlight> fareFlightRedisUtil;

    @Override
    public void cacheFareFlight(String generatedId, FareFlight fareFlight) {
       fareFlightRedisUtil.putList(generatedId, fareFlight);
    }

    @Override
    public List<FareFlight> findFareFlightByGenerateId(String generatedId, long i, long j) {
        List<FareFlight> fareFlights = fareFlightRedisUtil.getListRange(generatedId, i, j);
        return fareFlights;
    }

    @Override
    public long getSizeOfFareFlights(String generatedId) {
        return fareFlightRedisUtil.getListSize(generatedId);
    }



}
