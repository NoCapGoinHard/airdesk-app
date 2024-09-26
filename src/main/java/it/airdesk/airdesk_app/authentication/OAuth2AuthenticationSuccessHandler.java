package it.airdesk.airdesk_app.authentication;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import it.airdesk.airdesk_app.exceptions.NoSuchCredentialsException;
import it.airdesk.airdesk_app.exceptions.NoSuchUserException;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Extract OIDC user information
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String givenName = oidcUser.getGivenName();
        String familyName = oidcUser.getFamilyName();

        logger.info("User authenticated with OAuth2 provider. Email: {}, Given Name: {}, Family Name: {}", email, givenName, familyName);

        // Check if user already exists in the database
        User user;
        try {
            user = userService.findByEmail(email);
            logger.info("User found in the database with email: {}", email);
        } catch (NoSuchUserException e) {
            // If the user does not exist, create a new user
            logger.info("No existing user found with email: {}. Creating a new user.", email);
            user = new User();
            user.setName(givenName);
            user.setSurname(familyName);
            user.setEmail(email);

            // Save the user
            userService.save(user); // Persist the new user to the database
            logger.info("New user created and saved with email: {}", email);
        }

        // Now, let's check if the credentials exist
        String username = "USERNAMEof" + email;  // Ensure the same username format is used for the search
        try {
            credentialsService.findByUsername(username);  // Find credentials using the constructed username
            logger.info("Credentials found for username: {}", username);
        } catch (NoSuchCredentialsException e) {
            // If credentials don't exist, create and save new credentials
            logger.info("No credentials found for username: {}. Creating new credentials.", username);
            Credentials credentials = new Credentials();
            credentials.setUsername(username);  // Use the same username format
            credentials.setPassword(passwordEncoder.encode("OIDC_USER")); // Placeholder password
            credentials.setUser(user); // Associate credentials with the user
            credentials.setRole(Credentials.USER);

            // Save credentials
            credentialsService.save(credentials);
            logger.info("New credentials created and saved for username: {}", username);
        }

        // Redirect to a specific page after success
        logger.info("OAuth2 authentication successful. Redirecting...");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
