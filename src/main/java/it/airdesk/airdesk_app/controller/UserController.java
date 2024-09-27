package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;

@Controller
public class UserController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

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
            user.setCompany(updatedUser.getCompany());
            user.setAddress(updatedUser.getAddress());

            // Save the updated user to the database
            userService.save(user);

            // Add a success message or redirect
            model.addAttribute("success", "User details updated successfully.");
            return "redirect:/editUser";
        }
        return "redirect:/login";
    }

}
