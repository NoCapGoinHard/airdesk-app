package it.airdesk.airdesk_app.repository.auth;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
	
    public Optional<Admin> findByEmail(String email);

}
