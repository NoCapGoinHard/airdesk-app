package it.airdesk.airdesk_app.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.exceptions.NoSuchUserException;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;

@Controller
public class AuthenticationController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    @Autowired
    private CredentialsService credentialsService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login() {
        return "auth/login.html";
    }

    @GetMapping("/success")
    public String getIndexAfterLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            // Handle OAuth2 OIDC user
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            
            String email = oidcUser.getEmail();
            try {
                User existingUser = userService.findByEmail(email);
                logger.info("OAuth2 User '{}' logged in successfully", existingUser.getEmail());

                model.addAttribute("name", existingUser.getName());
                model.addAttribute("email", existingUser.getEmail());
            } catch (NoSuchUserException e) {
                logger.info("New OAuth2 user detected, redirecting to registration page");
                return "redirect:/register";  // Redirect to registration
            }
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
            User user = credentials.getUser();
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
        }
        return "index.html";
    }

    @GetMapping("/register")
    public String register(Model model) {
        logger.info("Accessing registration page");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();

        if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            // Pre-fill fields for OIDC users
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            user.setName(oidcUser.getGivenName());
            user.setSurname(oidcUser.getFamilyName());
            user.setEmail(oidcUser.getEmail());
            if (oidcUser.getClaims().containsKey("birthdate")) {
                user.setBirthDate(LocalDate.parse(oidcUser.getClaim("birthdate")));  // If birthdate is shared
            }
            model.addAttribute("isOidc", true); // For conditional display in the form
        } else {
            model.addAttribute("isOidc", false); // Standard user, fields are empty
        }

        model.addAttribute("user", user);
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        logger.info("Attempting to register user: {} {}", user.getName(), user.getSurname());

        if (!userBindingResult.hasErrors()) {
            Credentials credentials = new Credentials();
            credentials.setUsername(username);

            String encodedPassword = this.passwordEncoder.encode(password);
            credentials.setPassword(encodedPassword);
            credentials.setUser(user);
            credentials.setRole(Credentials.USER);

            userService.save(user);
            credentialsService.save(credentials);

            logger.info("Successfully registered user: {} with username: {}", user.getName(), credentials.getUsername());
            return "redirect:/";
        } else {
            logger.warn("User registration failed due to validation errors");
            return "auth/register.html";
        }
    }
}

