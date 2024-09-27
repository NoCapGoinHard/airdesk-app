package it.airdesk.airdesk_app.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.auth.AuthService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthService authService;


    @Autowired
    private CredentialsService credentialsService;
    

    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login.html";
    }

    @GetMapping("/success")
    public String getIndexAfterLogin(Model model) {
        // Use the method to get the authenticated user's credentials
        Optional<Credentials> credentialsOpt = credentialsService.getAuthenticatedUserCredentials();

        if (credentialsOpt.isPresent()) {
            User user = credentialsOpt.get().getUser();
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("surname", user.getSurname());
        } else {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        return "redirect:/";
    }


    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("isOidc", false);
        model.addAttribute("user", user);
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                                @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "password", required = false) String password,
                                Model model)    {
        if (!userBindingResult.hasErrors()) {
            // Use the common registration method
            authService.registerStandardUser(user, username, password);
            logger.info("User '{}' registered successfully", user.getEmail());
            return "redirect:/";
        } else {
            logger.warn("User registration failed due to validation errors");
            return "auth/register.html";
        }
    }
}

