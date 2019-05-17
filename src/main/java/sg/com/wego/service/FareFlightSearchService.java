package sg.com.wego.service;


import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;

public interface FareFlightSearchService {

    MetaSearchRequest validateMetaSearchRequest(MetaSearchRequest metaSearchRequest);

    MetasearchResponse findFlight(MetaSearchRequest metaSearchRequest);

    MetasearchResponse pollingFlight(String generatedId, long offset);



}
