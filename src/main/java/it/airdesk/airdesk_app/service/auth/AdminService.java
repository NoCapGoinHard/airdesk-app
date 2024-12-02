package it.airdesk.airdesk_app.service.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.Admin;
import it.airdesk.airdesk_app.repository.auth.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
    private AdminRepository adminRepository;

    /* ALTRI METODI PER LA CANCELLAZIONE DI UTENTI E LA MODIFICA DEI DATI DELL'UTENTE
     *  PROBABILMENTE ANDRANNO IMPLEMENTATI  */
    public void save(Admin admin) {
        this.adminRepository.save(admin);
    }
    
    // Find administrator by email or throw NoSuchUserException if not found
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
