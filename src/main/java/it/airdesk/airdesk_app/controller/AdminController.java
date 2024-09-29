package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.service.FacilityService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
@RequestMapping("/admin")  //EVERY ENDPOINT WILL START WITH '/admin/rest of it'
public class AdminController {
    
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private FacilityService facilityService;


    // Render the Admin Dashboard page
    @GetMapping("/dashboard")
    public String getAdminDashboard(Model model) {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

        if (credentials != null && credentials.getRole().equals(Credentials.ADMIN)) {
            // Admin is authenticated, show the dashboard
            return "admin/adminDashboard";
        }

        return "redirect:/login";  // Redirect to login if not authenticated
    }

    // Route to manage facilities (shows the list of facilities)
    @GetMapping("/manageFacilities")
    public String manageFacilities(Model model) {
        // Get the authenticated admin's credentials
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
        
        if (credentials == null || !credentials.isAdmin()) {
            return "redirect:/login";
        }

        // Fetch the facilities for the admin's company
        Long companyId = credentials.getUser().getCompany().getId();
        model.addAttribute("facilities", facilityService.findByCompanyId(companyId));

        return "admin/manageFacilities";
    }

    // View facility details
    @GetMapping("/facility/{id}")
    public String facilityPage(@PathVariable Long id, Model model) {
        Facility facility = facilityService.findById(id);
        model.addAttribute("facility", facility);
        return "admin/facilityPage";
    }
}
