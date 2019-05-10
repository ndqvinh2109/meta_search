package sg.com.wego.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.com.wego.model.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

}
