package sg.com.wego.service;


import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.model.MetasearchDto;

import java.util.List;

public interface MetaSearchService {

    void validate(MetaSearchCriteria metaSearchCriteria);

    List<FareFlight> findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) throws InterruptedException;

    MetasearchDto pollingFlight(String generatedId, long offset);



}
