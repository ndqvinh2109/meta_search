package sg.com.wego.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.com.wego.model.Airport;
import sg.com.wego.service.MetaSearchService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MetaSearchResource {

    @Autowired
    private MetaSearchService metaSearchService;

    @GetMapping("/airports")
    public List<Airport> getAllAiports() {
        return metaSearchService.getAllAirports();
    }
}
