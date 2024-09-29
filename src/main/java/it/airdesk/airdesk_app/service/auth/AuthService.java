package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.model.dataTypes.Address;
import it.airdesk.airdesk_app.service.CompanyService;

@Service
public class AuthService {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    // Register standard users with username and password ONLY
    public void registerStandardUser(User user, String username, String password) {
        Credentials credentials = new Credentials();
        credentials.setUser(user);
        credentials.setRole(Credentials.USER);
        credentials.setUsername(username);
        credentials.setPassword(passwordEncoder.encode(password));
    
        // Save the user and credentials
        userService.save(user);
        credentialsService.save(credentials);
    }
}