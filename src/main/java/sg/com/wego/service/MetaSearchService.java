package sg.com.wego.service;


import sg.com.wego.model.MetasearchDto;

public interface MetaSearchService {

    void findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId);

    MetasearchDto pollingFlight(String generatedId, long offset);

}
