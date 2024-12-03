package it.airdesk.airdesk_app.repository.auth;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.Host;

@Repository
public interface HostRepository extends CrudRepository<Host, Long> {
	
    public Optional<Host> findByEmail(String email);

}
