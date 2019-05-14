package sg.com.wego.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.com.wego.model.MetasearchDto;
import sg.com.wego.service.MetaSearchCriteria;
import sg.com.wego.service.MetaSearchService;

import java.util.UUID;


@RestController
@RequestMapping("/api")
public class MetaSearchResource {

    private static final Logger logger = LogManager.getLogger(MetaSearchResource.class);

    @Autowired
    private MetaSearchService metaSearchService;


    @PostMapping(path = "/flights", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> searchOneWayTicket(@RequestBody MetaSearchCriteria metaSearchCriteria) throws InterruptedException {
        metaSearchService.validate(metaSearchCriteria);

        String generatedId = UUID.randomUUID().toString();
        metaSearchService.findFlight(metaSearchCriteria, generatedId);
        return ResponseEntity.ok().body(generatedId);
    }

    @GetMapping("/flights/{generatedId}/{offset}")
    public ResponseEntity<MetasearchDto> polllingScheduleProvider(@PathVariable String generatedId, @PathVariable Long offset) {
        return ResponseEntity.ok().body(metaSearchService.pollingFlight(generatedId, offset));
    }

}
