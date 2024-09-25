package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.exceptions.NoSuchUserException;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.repository.auth.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        this.userRepository.save(user);
    }

    // Find user by email or throw NoSuchUserException if not found
    public User findByEmail(String email) throws NoSuchUserException {

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchUserException("No user found with email: " + email));
    }

}
