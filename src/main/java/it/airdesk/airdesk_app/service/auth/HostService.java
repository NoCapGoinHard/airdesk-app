package it.airdesk.airdesk_app.service.auth;

import java.util.List;
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
    public List<Host> findByEmail(String email) {
        return hostRepository.findByEmail(email);
    }

    /*
     * Metodo di aiuto alla classe Validator per il controllo della non esistenza nella lista degli host 
     * gia' registrati di un altro host registrato con la stessa email dell'host da inserire
     */
	public boolean alreadyExists(Host host) {
		List<Host> hosts = this.hostRepository.findByEmail(host.getEmail());
		if (hosts.size() > 0)
			return true;
		else 
			return false;
	}
}
