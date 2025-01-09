package it.airdesk.airdesk_app.repository.auth;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.IntermediateHost;

@Repository
public interface IntermediateHostRepository extends CrudRepository<IntermediateHost, Long> {
	
    public List<IntermediateHost> findByEmail(String email);

	public List<IntermediateHost> findByNameAndSurname(String name, String surname);

}
