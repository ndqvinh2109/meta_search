package sg.com.wego.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.com.wego.entity.Airport;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findAllByCode(String airportCode);
}
