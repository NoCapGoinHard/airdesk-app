package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.airdesk.airdesk_app.model.Booking;
import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.BookingService;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;

@Controller
public class UserController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private BookingService bookingService;


    // Display the user's profile page
    @GetMapping("/userPage")
    public String userPage(Model model) {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

        if (credentials != null) {
            User user = credentials.getUser();
            model.addAttribute("user", user);  // Add user to the model for display
            return "user.html";  // Return the view for user profile (user.html)
        }
        return "redirect:/login";
    }

    // Display the form with pre-filled user data
    @GetMapping("/editUser")
    public String editUser(Model model) {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

        if (credentials != null) {
            User user = credentials.getUser();
            model.addAttribute("user", user);  // Add user to the model to pre-fill the form
            return "forms/editUser.html";
        }
        return "redirect:/login";
    }

    // Process form submission to update user data
    @PostMapping("/editUser")
    public String editUser(@ModelAttribute("user") User updatedUser, Model model) {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

        if (credentials != null) {
            User user = credentials.getUser();

            // Update user details from form data
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            user.setBirthDate(updatedUser.getBirthDate());

            // Save or find company and then associate it with the user
            Company company = companyService.findOrCreateCompanyByName(updatedUser.getCompany().getName());
            user.setCompany(company);

            // Save the updated user to the database
            userService.save(user);

            // Add a success message or redirect
            model.addAttribute("success", "User details updated successfully.");
            return "redirect:/userPage";
        }
        return "redirect:/login";
    }

    @GetMapping("/bookingReceipt/{id}")
    public String viewBookingReceipt(@PathVariable("id") Long bookingId, Model model) {
        // Fetch the booking by ID
        Booking booking = bookingService.findById(bookingId);
        
        if (booking != null) {
            model.addAttribute("booking", booking);
            return "bookingReceipt";  // Return the receipt template
        }

        return "redirect:/userPage";  // Redirect if booking not found
    }
}
