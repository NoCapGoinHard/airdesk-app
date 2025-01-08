package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.Host;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;

@Service
public class AuthService {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

	@Autowired
    private HostService hostService;

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
    
    // Register basic host users with username and password ONLY
    public void registerHostUser(Host host, String username, String password) {
        Credentials credentials = new Credentials();
        credentials.setHost(host);
        credentials.setRole(Credentials.HOST);
        credentials.setUsername(username);
        credentials.setPassword(passwordEncoder.encode(password));
    
        // Save the user and credentials
        hostService.save(host);
        credentialsService.save(credentials);
    }
    
    // Register intermediate host users with username and password ONLY
    public void registerIntermediateHostUser(Host host, String username, String password) {
        Credentials credentials = new Credentials();
        credentials.setHost(host);
        credentials.setRole(Credentials.INTERMEDIATE_HOST);
        credentials.setUsername(username);
        credentials.setPassword(passwordEncoder.encode(password));
    
        // Save the user and credentials
        hostService.save(host);
        credentialsService.save(credentials);
    }
    
    public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public HostService getHostService() {
		return hostService;
	}

	public void setHostService(HostService hostService) {
		this.hostService = hostService;
	}
}