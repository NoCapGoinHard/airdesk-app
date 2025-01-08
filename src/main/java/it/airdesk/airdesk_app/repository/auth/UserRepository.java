package it.airdesk.airdesk_app.repository.auth;


import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.auth.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
}
