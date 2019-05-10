package sg.com.wego.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.com.wego.dao.AirportRepository;
import sg.com.wego.model.Airport;

import java.util.List;

@Service
public class MetaSearchServiceImpl  implements MetaSearchService {

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

}
