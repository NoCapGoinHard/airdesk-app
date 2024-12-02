package it.airdesk.airdesk_app.service.auth;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.repository.auth.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    /* ALTRI METODI PER LA CANCELLAZIONE DI UTENTI E LA MODIFICA DEI DATI DELL'UTENTE
     *  PROBABILMENTE ANDRANNO IMPLEMENTATI  */
    public void save(User user) {
        this.userRepository.save(user);
    }
    
    // Find user by email or throw NoSuchUserException if not found
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
