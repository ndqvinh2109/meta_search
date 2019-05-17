package sg.com.wego.service.searchable;

import org.springframework.stereotype.Service;
import sg.com.wego.entity.Schedule;
import sg.com.wego.model.MetasearchResponse;

import java.util.List;
import java.util.Map;

@Service
public class HotelSearchEngine implements SearchEngine<Schedule> {

    @Override
    public void simulateHttpRequestForCachingFareFlight(Map<String, List<Schedule>> scheduleMap, String generatedId) {
        // not supported yet
    }

    @Override
    public MetasearchResponse pollingFlight(String generatedId, long offset) {
        // not supported yet
        return null;
    }
}
