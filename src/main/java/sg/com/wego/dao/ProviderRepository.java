package sg.com.wego.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.com.wego.model.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

}
