package sg.com.wego.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.com.wego.model.MetaSearchRequest;
import sg.com.wego.model.MetasearchResponse;
import sg.com.wego.service.FareFlightSearchService;


@RestController
@RequestMapping("/api")
public class MetaSearchResource {

    private static final Logger logger = LogManager.getLogger(MetaSearchResource.class);

    @Autowired
    private FareFlightSearchService fareFlightSearchService;


    @PostMapping(path = "/flights", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MetasearchResponse> searchFareFlght(@RequestBody MetaSearchRequest metaSearchRequest) {
        fareFlightSearchService.validateMetaSearchRequest(metaSearchRequest);
        return ResponseEntity.ok().body(fareFlightSearchService.findFlight(metaSearchRequest));
    }

    @GetMapping("/flights/{generatedId}")
    public ResponseEntity<MetasearchResponse> polllingScheduleProvider(@PathVariable String generatedId, @RequestParam(defaultValue = "0") Long offset) {
        return ResponseEntity.ok().body(fareFlightSearchService.pollingFlight(generatedId, offset));
    }

}
