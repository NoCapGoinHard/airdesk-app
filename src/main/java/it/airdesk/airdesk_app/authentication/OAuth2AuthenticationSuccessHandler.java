package it.airdesk.airdesk_app.authentication;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String givenName = oidcUser.getGivenName();
        String familyName = oidcUser.getFamilyName();

        logger.info("User authenticated with OAuth2 provider. Email: {}, Given Name: {}, Family Name: {}", email, givenName, familyName);

        // Check if user already exists in the database using Optional
        Optional<User> userOpt = userService.findByEmail(email);
        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
            logger.info("User found in the database with email: {}", email);
        } else {
            logger.info("No existing user found with email: {}. Creating a new user.", email);
            user = new User();
            user.setName(givenName);
            user.setSurname(familyName);
            user.setEmail(email);
            userService.save(user);  // Persist the new user to the database
            logger.info("New user created and saved with email: {}", email);
        }

        // Now check if credentials exist using the username format
        String oidcUsername = "USERNAMEof" + email;
        Optional<Credentials> credentialsOpt = credentialsService.findByUsername(oidcUsername);

        if (credentialsOpt.isPresent()) {
            logger.info("Credentials found for username: {}", oidcUsername);
        } else {
            logger.info("No credentials found for username: {}. Creating new credentials.", oidcUsername);
            Credentials credentials = new Credentials();
            credentials.setUsername(oidcUsername);  // Use the correct username format
            credentials.setPassword(passwordEncoder.encode("OIDC_USER"));  // Placeholder password
            credentials.setUser(user);  // Associate credentials with the user
            credentials.setRole(Credentials.USER);
            credentialsService.save(credentials);  // Save credentials
            logger.info("New credentials created and saved for username: {}", oidcUsername);
        }

        // Redirect to a specific page after success
        logger.info("OAuth2 authentication successful. Redirecting...");
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
