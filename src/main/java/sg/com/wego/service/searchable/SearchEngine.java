package sg.com.wego.service.searchable;

import sg.com.wego.model.MetasearchResponse;

import java.util.List;
import java.util.Map;

public interface SearchEngine<T> {
   void simulateHttpRequestForCachingFareFlight(Map<String, List<T>> scheduleMap, String generatedId);
   MetasearchResponse pollingFlight(String generatedId, long offset);
}
