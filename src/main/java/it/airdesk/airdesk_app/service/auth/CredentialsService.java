package it.airdesk.airdesk_app.service.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.repository.auth.CredentialsRepository;

@Service
public class CredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    public Optional<Credentials> findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }

    public void save(Credentials credentials) {
        this.credentialsRepository.save(credentials);
    }


    /**
     * Utility method to retrieve authenticated user's credentials.
     * INSERTED HERE FOR FURTHER INVOCATION, FROM PATTERN INDIRECTION
     * @return Optional<Credentials> - authenticated user's credentials, if available
     */
    public Optional<Credentials> getAuthenticatedUserCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            Credentials credentials = null;

            // Handle UserDetails or OIDC user
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                logger.info("Attempting to retrieve credentials for UserDetails with username: {}", username);
                credentials = credentialsRepository.findByUsername(username).orElse(null);
            } else if (principal instanceof DefaultOidcUser) {
                String email = ((DefaultOidcUser) principal).getEmail();
                logger.info("Attempting to retrieve credentials for OIDC user with email: {}", email);
                credentials = credentialsRepository.findByUsername("USERNAMEof" + email).orElse(null);
            }

            if (credentials != null) {
                logger.info("Credentials found for user.");
            } else {
                logger.warn("Credentials not found for the authenticated user.");
            }

            return Optional.ofNullable(credentials);
        } else {
            logger.warn("No authenticated user found.");
        }

        return Optional.empty();
    }
    
    /**
     * Utility method to retrieve authenticated admin's credentials.
     * INSERTED HERE FOR FURTHER INVOCATION, FROM PATTERN INDIRECTION
     * @return Optional<Credentials> - authenticated admin's credentials, if available
     */
    /* QUESTO È PER ORA UN METODO SUPERFLUO*/
    
    public Optional<Credentials> getAuthenticatedAdminCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            Credentials credentials = null;

            // Handle AdminDetails or OIDC user
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                logger.info("Attempting to retrieve credentials for UserDetails with username: {}", username);
                credentials = credentialsRepository.findByUsername(username).orElse(null);
            } else if (principal instanceof DefaultOidcUser) {
                String email = ((DefaultOidcUser) principal).getEmail();
                logger.info("Attempting to retrieve credentials for OIDC user with email: {}", email);
                credentials = credentialsRepository.findByUsername("USERNAMEof" + email).orElse(null);
            }

            if (credentials != null) {
                logger.info("Credentials found for user.");
            } else {
                logger.warn("Credentials not found for the authenticated user.");
            }

            return Optional.ofNullable(credentials);
        } else {
            logger.warn("No authenticated user found.");
        }

        return Optional.empty();
    }
}



