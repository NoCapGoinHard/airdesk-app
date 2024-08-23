package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.repository.auth.CredentialsRepository;

@Service
public class CredentialsService {
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    public Credentials findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }

    public void save(Credentials credentials) {
        this.credentialsRepository.save(credentials);
    }
}
