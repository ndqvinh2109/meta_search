package sg.com.wego.service.searchable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SearchEngineFactory {
    final static Map<SearchEngineType, Supplier<SearchEngine>> map = new HashMap<>();

    static {
        map.put(SearchEngineType.FLIGHT, FlightSearchEngine::new);
    }

    public static SearchEngine getSearchEngine(SearchEngineType searchEngineType) {
        Supplier<SearchEngine> searchEngine = map.get(searchEngineType);
        if(searchEngine != null) {
            return searchEngine.get();
        }

        throw new IllegalArgumentException("No such type " + searchEngineType.name());
    }

}
