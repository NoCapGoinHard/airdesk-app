package it.airdesk.airdesk_app.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.auth.Host;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.auth.AuthService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
public class AuthenticationController { //this class handles the authentication procedure for STANDARD USERS

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private CompanyService companyService;
    

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
            Credentials credentials = credentialsOpt.get();

            // Redirect based on role
            if (credentials.isAdmin()) {
                logger.info("Admin logged in, redirecting to the admin dashboard");
                return "redirect:/admin/dashboard";
            } else {
                logger.info("User logged in, redirecting to index");
                return "redirect:/";
            }
        }

        // If not authenticated, redirect to login
        return "redirect:/login";
    }

    /* Methods to register a normal user*/
    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        user.setCompany(new Company());
        model.addAttribute("isOidc", false);
        model.addAttribute("user", user);
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {

        //Users can register themselves as freelancers, by ticking the checkbox related to this variable
        boolean isFreelancer = freelancerChecked != null && freelancerChecked.equals("on");

        if (!userBindingResult.hasErrors()) {
            // If the user checked the "Freelancer" box, assign the "FREELANCER" company
            if (isFreelancer) {
                Company freelancerCompany = companyService.findOrCreateCompanyByName("FREELANCER");
                user.setCompany(freelancerCompany);
            } else {
                // If the user is not a freelancer and has not entered a company, handle the error
                if (user.getCompany() == null || user.getCompany().getName().trim().isEmpty()) {
                    model.addAttribute("error", "Company name must be provided unless working as a freelancer.");
                    return "auth/register.html";
                }
                // Otherwise, find or create the company the user entered
                Company company = companyService.findOrCreateCompanyByName(user.getCompany().getName());
                user.setCompany(company);
            }

            // Use the simplified registration method
            authService.registerStandardUser(user, username, password);
            logger.info("User '{}' registered successfully", user.getEmail());
            return "redirect:/";
        } else {
            logger.warn("User registration failed due to validation errors");
            return "auth/register.html";
        }
    }
    
    /* Methods to register an Host user */
    @GetMapping("/hostRegister")
    public String register2(Model model) {
        Host host = new Host();
        host.setCompany(new Company());
        model.addAttribute("isOidc", false);
        model.addAttribute("host", host);
        return "auth/registerHost.html";
    }

    @PostMapping("/hostRegister")
    public String registerHost(@ModelAttribute("host") Host host, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {

        //Hosts can register themselves as freelancers, by ticking the checkbox related to this variable
        boolean isFreelancer = freelancerChecked != null && freelancerChecked.equals("on");

        if (!userBindingResult.hasErrors()) {
            // If the user checked the "Freelancer" box, assign the "FREELANCER" company
            if (isFreelancer) {
                Company freelancerCompany = companyService.findOrCreateCompanyByName("FREELANCER");
                host.setCompany(freelancerCompany);
            } else {
                // If the user is not a freelancer and has not entered a company, handle the error
                if (host.getCompany() == null || host.getCompany().getName().trim().isEmpty()) {
                    model.addAttribute("error", "Company name must be provided unless working as a freelancer.");
                    return "auth/registerHost.html";
                }
                // Otherwise, find or create the company the user entered
                Company company = companyService.findOrCreateCompanyByName(host.getCompany().getName());
                host.setCompany(company);
            }

            // Use the simplified registration method
            authService.registerHostUser(host, username, password);
            logger.info("Host '{}' registered successfully", host.getEmail());
            return "redirect:/";
        } else {
            logger.warn("Admin registration failed due to validation errors");
            return "auth/registerHost.html";
        }
    }
}

