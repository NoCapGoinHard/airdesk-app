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
import it.airdesk.airdesk_app.model.auth.IntermediateHost;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.auth.AuthService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.HostService;
import it.airdesk.airdesk_app.service.auth.IntermediateHostService;
import it.airdesk.airdesk_app.service.auth.UserService;

@Controller
public class AuthenticationController { //this class handles the authentication procedure for STANDARD USERS

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HostService hostService;
    
    @Autowired
    private IntermediateHostService intermediateHostService;

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
    
    @GetMapping("/chooseService")
    public String chooseService() {
        return "chooseService.html";
    }
    
    @GetMapping("/success")
    public String getIndexAfterLogin(Model model) {
        // Use the method to get the authenticated user's credentials
        Optional<Credentials> credentialsOpt = credentialsService.getAuthenticatedUserCredentials();

        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();

            // Redirect based on role
            if (credentials.isHost() || credentials.isIntermediateHost()) {
                logger.info("Host logged in, redirecting to the admin dashboard");
                return "redirect:/host/dashboard";
            } else {
                logger.info("User logged in, redirecting to index");
                return "redirect:/";
            }
        }

        // If not authenticated, redirect to login
        return "redirect:/login";
    }

    // METODI CONTROLLER PER LA REGISTRAZIONE DI UN UTENTE USER
    
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
        
        if (!userBindingResult.hasErrors() && !userService.alreadyExists(user)) {
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
            /* 
             * ora si viene correttamente reindirizzati sulla pagina di registrazione, per permettere di
             * registrarsi con un'altra email o effettuare il login.
             * */
            return "redirect:/emailErrorRegister";
        }
    }
    
    /**
     * Metodi per il mapping di una pagina di registrazione in cui appare un messaggio di errore nel campo
     * "email" nel caso in cui si stia cercando di utilizzare una email gia' in uso. Il messaggio suggerisce
     * di cambiare email o di effettuare il login se si e' gia' registrati.
     * @param model
     * @return
     */
	@GetMapping("/emailErrorRegister")
	public String emailErrorRegister(Model model) {
		User user = new User();
		user.setCompany(new Company());
		model.addAttribute("isOidc", false);
		model.addAttribute("user", user);
		return "auth/emailErrorRegister.html";
	}
	
    @PostMapping("/emailErrorRegister")
    public String emailErrorRegisterUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {

        //Users can register themselves as freelancers, by ticking the checkbox related to this variable
        boolean isFreelancer = freelancerChecked != null && freelancerChecked.equals("on");
        
        /*
         * Bisogna unificare il metodo di verifica alreadyExists(), che attualmente si trova nelle classi
         * UserService e HostService, per tutti gli attori presenti, altrimenti sono possibili due utenti
         * con stesso username e password. Il che crea un conflitto successivamente a livello di login.
         * ANCORA MIGLIORE: fare il controllo sulle credenziali (come credevo di aver gia' fatto)
         */
        if (!userBindingResult.hasErrors() && !userService.alreadyExists(user)) {
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
            /* ora si viene correttamente reindirizzati sulla pagina di registrazione, per permettere di
             * con un'altra email o effettuare il login. Manca ancora il messaggio di errore sul sito */
            return "redirect:/emailErrorRegister";
        }
    }
    
    /**
     *  Metodi Controller per la registrazione di utenti con permessi da HOST e varie licenze di gestione
     * @param model
     * @return
     */
    
    /* Methods to register an Host user */
    @GetMapping("/hostRegister")
    public String registerBasic(Model model) {
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

        if (!userBindingResult.hasErrors() && !hostService.alreadyExists(host)) {
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
            logger.warn("Host registration failed due to validation errors");
            return "auth/registerHost.html";
        }
    }
    
    @GetMapping("/intermediateHostRegister")
    public String registerIntermediate(Model model) {
        IntermediateHost intermediateHost = new IntermediateHost();
        intermediateHost.setCompany(new Company());
        model.addAttribute("isOidc", false);
        model.addAttribute("intermediateHost", intermediateHost);
        return "auth/registerIntermediateHost.html";
    }

    @PostMapping("/intermediateHostRegister")
    public String registerIntermediateHost(@ModelAttribute("intermediateHost") IntermediateHost intermediateHost, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {

        //Hosts can register themselves as freelancers, by ticking the checkbox related to this variable
        boolean isFreelancer = freelancerChecked != null && freelancerChecked.equals("on");

        if (!userBindingResult.hasErrors() && !intermediateHostService.alreadyExists(intermediateHost)) {
            // If the user checked the "Freelancer" box, assign the "FREELANCER" company
            if (isFreelancer) {
                Company freelancerCompany = companyService.findOrCreateCompanyByName("FREELANCER");
                intermediateHost.setCompany(freelancerCompany);
            } else {
                // If the user is not a freelancer and has not entered a company, handle the error
                if (intermediateHost.getCompany() == null || intermediateHost.getCompany().getName().trim().isEmpty()) {
                    model.addAttribute("error", "Company name must be provided unless working as a freelancer.");
                    return "auth/registerIntermediateHost.html";
                }
                // Otherwise, find or create the company the user entered
                Company company = companyService.findOrCreateCompanyByName(intermediateHost.getCompany().getName());
                intermediateHost.setCompany(company);
            }

            // Use the simplified registration method
            authService.registerIntermediateHostUser(intermediateHost, username, password);
            logger.info("Host '{}' registered successfully", intermediateHost.getEmail());
            return "redirect:/";
        } else {
            logger.warn("Host registration failed due to validation errors");
            return "auth/registerIntermediateHost.html";
        }
    }
    
    /**
     * Metodi per il mapping di una pagina di registrazione in cui appare un messaggio di errore nel campo
     * "email" nel caso in cui si stia cercando di utilizzare una email gia' in uso. Il messaggio suggerisce
     * di cambiare email o di effettuare il login se si e' gia' registrati.
     * @param model
     * @return
     */
	@GetMapping("/emailErrorHostRegister")
	public String emailErrorHostRegister(Model model) {
		Host host = new Host();
		host.setCompany(new Company());
		model.addAttribute("isOidc", false);
		model.addAttribute("host", host);
		return "auth/emailErrorHostRegister.html";
	}
	
    @PostMapping("/emailErrorHostRegister")
    public String emailErrorRegisterHost(@ModelAttribute("host") Host host, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {

        //Users can register themselves as freelancers, by ticking the checkbox related to this variable
        boolean isFreelancer = freelancerChecked != null && freelancerChecked.equals("on");
        
        /*
         * Bisogna unificare il metodo di verifica alreadyExists(), che attualmente si trova nelle classi
         * UserService e HostService, per tutti gli attori presenti, altrimenti sono possibili due utenti
         * con stesso username e password. Il che crea un conflitto successivamente a livello di login.
         * ANCORA MIGLIORE: fare il controllo sulle credenziali (come credevo di aver gia' fatto)
         */
        if (!userBindingResult.hasErrors() && !hostService.alreadyExists(host)) {
            // If the user checked the "Freelancer" box, assign the "FREELANCER" company
            if (isFreelancer) {
                Company freelancerCompany = companyService.findOrCreateCompanyByName("FREELANCER");
                host.setCompany(freelancerCompany);
            } else {
                // If the user is not a freelancer and has not entered a company, handle the error
                if (host.getCompany() == null || host.getCompany().getName().trim().isEmpty()) {
                    model.addAttribute("error", "Company name must be provided unless working as a freelancer.");
                    return "auth/register.html";
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
            logger.warn("User registration failed due to validation errors");
            /* ora si viene correttamente reindirizzati sulla pagina di registrazione, per permettere di
             * con un'altra email o effettuare il login. Manca ancora il messaggio di errore sul sito */
            return "redirect:/emailErrorHostRegister";
        }
    }
}

