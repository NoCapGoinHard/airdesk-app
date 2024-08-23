package it.airdesk.airdesk_app.controller;

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
        return "index.html";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                                @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                Model model) {
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()){
            userService.save(user);
            credentials.setUser(user);
            credentials.setRole(Credentials.USER);
            credentialsService.save(credentials);
            model.addAttribute("user", user);
            return "redirect:/";
        }
        else return "register.html";

    }
}
