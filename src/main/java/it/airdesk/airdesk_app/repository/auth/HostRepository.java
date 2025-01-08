package it.airdesk.airdesk_app.repository.auth;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.Host;

@Repository
public interface HostRepository extends CrudRepository<Host, Long> {
	
    public List<Host> findByEmail(String email);

	public List<Host> findByNameAndSurname(String name, String surname);

}
