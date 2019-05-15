package sg.com.wego.cache;

import sg.com.wego.cache.entity.FareFlight;

import java.util.List;

public interface FareFlightCacheManager {
    void cacheFareFlight(String generateId, FareFlight fareFlight);
    List<FareFlight> findFareFlightByGenerateId(String generatedId, long i, long j);
    long getSizeOfFareFlights(String generateId);

}
