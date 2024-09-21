package it.airdesk.airdesk_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/login")
    public String login() {
        return "auth/login.html";
    }

    @GetMapping("/success")
    public String getIndexAfterLogin(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = this.credentialsService.findByUsername(userDetails.getUsername());
        logger.info("User '{}' logged in successfully with role '{}'", credentials.getUsername(), credentials.getRole());
        return "index.html";
    }

    @GetMapping("/register")
    public String register(Model model) {
        logger.info("Accessing registration page");
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                                @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                Model model) {
        
        logger.info("Attempting to register user: {} {}", user.getName(), user.getSurname());

        logger.debug("User object: {}", user);
        logger.debug("Credentials object: {}", credentials);  // needed to check if credentials was null here during registration implementation
        
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()){
            logger.debug("User address details: {}, {}, {}, {}, {}", 
            user.getAddress().getStreet(),
            user.getAddress().getCity(),
            user.getAddress().getState(),
            user.getAddress().getCountry(),
            user.getAddress().getPostalCode());

            userService.save(user);
            credentials.setUser(user);
            credentials.setRole(Credentials.USER);
            credentialsService.save(credentials);

            logger.info("Successfully registered user: {} with username: {}", user.getName(), credentials.getUsername());

            model.addAttribute("user", user);
            return "redirect:/";
        }
        else {
            logger.warn("User registration failed due to validation errors");
            if (userBindingResult.hasErrors()) logger.debug("User form validation errors: {}", userBindingResult.getAllErrors());
            if (credentialsBindingResult.hasErrors()) logger.debug("Credentials form validation errors: {}", credentialsBindingResult.getAllErrors());

            return "auth/register.html";
        }

    }
}
