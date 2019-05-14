package sg.com.wego.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sg.com.wego.dto.MetasearchDto;
import sg.com.wego.service.MetaSearchCriteria;
import sg.com.wego.service.MetaSearchService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api")
public class MetaSearchResource {

    private static final Logger logger = LogManager.getLogger(MetaSearchResource.class);

    @Autowired
    private MetaSearchService metaSearchService;


    @PostMapping(path = "/flights", consumes = "application/json", produces = "application/json")
    public String searchOneWayTicket(@RequestBody MetaSearchCriteria metaSearchCriteria) throws ExecutionException, InterruptedException {
        String generatedId = UUID.randomUUID().toString();
        metaSearchService.findFlight(metaSearchCriteria, generatedId);
        return generatedId;
    }

    @GetMapping("/flights/{generatedId}/{offset}")
    public MetasearchDto polllingScheduleProvider(@PathVariable String generatedId, @PathVariable Long offset) {
        return metaSearchService.pollingFlight(generatedId, offset);
    }



}
