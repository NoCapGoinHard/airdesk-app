package it.airdesk.airdesk_app.service.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.Host;
import it.airdesk.airdesk_app.repository.auth.HostRepository;

@Service
public class HostService {
	
	@Autowired
    private HostRepository hostRepository;

    /* ALTRI METODI PER LA CANCELLAZIONE DI UTENTI E LA MODIFICA DEI DATI DELL'UTENTE
     *  PROBABILMENTE ANDRANNO IMPLEMENTATI  */
    public void save(Host host) {
        this.hostRepository.save(host);
    }
    
    // Find administrator by email or throw NoSuchUserException if not found
    public Optional<Host> findByEmail(String email) {
        return hostRepository.findByEmail(email);
    }
}
