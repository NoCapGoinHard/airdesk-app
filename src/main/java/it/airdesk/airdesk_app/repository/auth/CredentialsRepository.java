package it.airdesk.airdesk_app.repository.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.Credentials;
@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long>{
    
}
