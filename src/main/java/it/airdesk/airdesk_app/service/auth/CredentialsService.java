package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.exceptions.NoSuchCredentialsException;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.repository.auth.CredentialsRepository;

@Service
public class CredentialsService {
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    public Credentials findByUsername(String username) throws NoSuchCredentialsException {
        return credentialsRepository.findByUsername(username)
            .orElseThrow(() -> new NoSuchCredentialsException("No credentials found for username: " + username));
    }

    public void save(Credentials credentials) {
        this.credentialsRepository.save(credentials);
    }
}
