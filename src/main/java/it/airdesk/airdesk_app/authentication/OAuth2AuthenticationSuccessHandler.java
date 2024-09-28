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

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.model.dataTypes.Address;
import it.airdesk.airdesk_app.repository.CompanyRepository;
import it.airdesk.airdesk_app.service.CompanyService;
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

    @Autowired
    private CompanyService companyService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Log if handler is triggered
        logger.info("OAuth2AuthenticationSuccessHandler triggered");

        // Extract OIDC user information
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String givenName = oidcUser.getGivenName();
        String familyName = oidcUser.getFamilyName();

        logger.info("User authenticated with OAuth2 provider. Email: {}, Given Name: {}, Family Name: {}", email, givenName, familyName);

        // Check if user already exists in the database
        User user;
        String username = "USERNAMEof" + email;  // Ensure the username is formatted correctly
        Credentials credentials = credentialsService.findByUsername(username).orElse(null);

        if (credentials == null) {
            // Create new user and credentials if they don't exist
            user = new User();
            user.setName(givenName);
            user.setSurname(familyName);
            user.setEmail(email);

            Company company = companyService.findByName("UNKNOWN").orElseGet(() -> {
                Company newCompany = new Company("UNKNOWN");
                companyService.save(newCompany);
                return newCompany;
            });
            user.setCompany(company);  // Assign "UNKNOWN" as default

            // Handle default address if missing
            Address address = new Address("Unknown");
            user.setAddress(address);

            userService.save(user);  //USER'S PERSISTENCE
            credentials = new Credentials();
            credentials.setUsername(username);
            credentials.setPassword(passwordEncoder.encode("OIDC_USER"));  // Placeholder password
            credentials.setUser(user);
            credentials.setRole(Credentials.USER);

            credentialsService.save(credentials);  // Save the new credentials
            logger.info("New user and credentials created for email: {}", email);
        } else {
            user = credentials.getUser();
            logger.info("Existing user found for email: {}", email);
        }

        // Redirect manually to the index after login
        logger.info("Redirecting to index page...");
        // After authentication success, redirect to the home page
        response.sendRedirect("/");
    }
}
