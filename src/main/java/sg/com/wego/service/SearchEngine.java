package sg.com.wego.service;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface SearchEngine<T> {
   void simulateHttpRequestForCachingFareFlight(Map<String, List<T>> scheduleMap, String generatedId);
}
