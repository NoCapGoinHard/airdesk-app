package it.airdesk.airdesk_app.service.auth;

import java.util.List;
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
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Metodo che verifica l'esistenza pregressa di una email uguale nel database
    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /*
     * Metodo di aiuto alla classe Validator per il controllo della non esistenza nella lista degli users 
     * gia' registrati di un altro user registrato con la stessa email dell'user da inserire
     */
	public boolean alreadyExists(User user) {
		List<User> users = this.userRepository.findByEmail(user.getEmail());
		if (users.size() > 0)
			return true;
		else 
			return false;
	}

}
