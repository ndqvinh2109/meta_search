package sg.com.wego.service;


import sg.com.wego.dto.MetasearchDto;

import java.util.concurrent.ExecutionException;

public interface MetaSearchService {

    void findFlight(MetaSearchCriteria metaSearchCriteria, String generatedId) throws InterruptedException, ExecutionException;

    MetasearchDto pollingFlight(String generatedId, long offset);

}
