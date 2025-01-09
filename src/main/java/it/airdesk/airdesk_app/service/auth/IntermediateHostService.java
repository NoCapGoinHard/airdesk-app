package it.airdesk.airdesk_app.service.auth;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.IntermediateHost;
import it.airdesk.airdesk_app.repository.auth.IntermediateHostRepository;

@Service
public class IntermediateHostService {
	
	@Autowired
    private IntermediateHostRepository intermediateHostRepository;

    /* ALTRI METODI PER LA CANCELLAZIONE DI UTENTI E LA MODIFICA DEI DATI DELL'UTENTE
     *  PROBABILMENTE ANDRANNO IMPLEMENTATI  */
    public void save(IntermediateHost intermediateHost) {
        this.intermediateHostRepository.save(intermediateHost);
    }
    
    // Find administrator by email or throw NoSuchUserException if not found
    public List<IntermediateHost> findByEmail(String email) {
        return intermediateHostRepository.findByEmail(email);
    }

    /*
     * Metodo di aiuto alla classe Validator per il controllo della non esistenza nella lista degli host 
     * gia' registrati di un altro host registrato con la stessa email dell'host da inserire
     */
	public boolean alreadyExists(IntermediateHost intermediateHost) {
		List<IntermediateHost> intermediateHosts = this.intermediateHostRepository.findByEmail(intermediateHost.getEmail());
		if (intermediateHosts.size() > 0)
			return true;
		else 
			return false;
	}
}
