package it.airdesk.airdesk_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.airdesk.airdesk_app.exceptions.NotFoundException;
import it.airdesk.airdesk_app.model.Booking;
import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.BookingService;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
    public String bookingReceipt(@PathVariable("id") Long bookingId, Model model) {
        Booking booking = bookingService.findById(bookingId);
        
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "errorPage";  // Return an error page or redirect appropriately
        }

        model.addAttribute("booking", booking);  // Add the booking to the model

        // Log the retrieved booking for debugging purposes
        logger.info("Displaying receipt for booking ID: {}", bookingId);

        return "bookingReceipt.html";  // Use the same template for both flows (right after making a booking and from userPage)
    }

    @GetMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable("id") Long bookingId, Model model) throws NotFoundException{
        Booking toDelete = bookingService.findById(bookingId);
        if(toDelete != null) {
            User user = credentialsService.getAuthenticatedUserCredentials().orElse(null).getUser();
            logger.info("User {} (ID: {}) attempting to delete booking with ID: {}", user.getEmail(), user.getId(), bookingId);
            user.removeBooking(toDelete);
            Workstation workstation = toDelete.getWorkstation();
            workstation.removeBooking(toDelete);
            bookingService.deleteById(bookingId);
            logger.info("Completed request to delete booking with ID: {}", bookingId);
            return "redirect:/userPage";
        }
        else throw new NotFoundException("Booking to delete not found");
        
    }
}
