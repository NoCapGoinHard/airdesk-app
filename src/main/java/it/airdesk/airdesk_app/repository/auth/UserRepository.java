package it.airdesk.airdesk_app.repository.auth;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.exceptions.NoSuchUserException;
import it.airdesk.airdesk_app.model.auth.User;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByEmail(String email) throws NoSuchUserException;
    
}
