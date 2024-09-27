package it.airdesk.airdesk_app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.exceptions.NoAvailableWorkstationException;
import it.airdesk.airdesk_app.model.Booking;
import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.repository.BookingRepository;
import it.airdesk.airdesk_app.service.auth.CredentialsService;
import it.airdesk.airdesk_app.service.auth.UserService;
import jakarta.transaction.Transactional;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Transactional
    public Booking createBooking(Booking booking, Long buildingId, String workstationType) throws NoAvailableWorkstationException {

        logger.info("Creating booking for building ID: {}, workstation type: {}, date: {}, start time: {}, end time: {}",
                    buildingId, workstationType, booking.getDate(), booking.getStartingTime(), booking.getEndingTime());

        // Fetch available workstations logic
        List<Workstation> availableWorkstations = workstationService.findAvailableWorkstations(
            buildingId,
            workstationType,
            booking.getDate(),
            booking.getStartingTime(),
            booking.getEndingTime(),
            booking.getDate().getDayOfWeek()
        );

        if (availableWorkstations.isEmpty()) {
            throw new NoAvailableWorkstationException("No available workstations for the selected parameters.");
        }

        Workstation assignedWorkstation = availableWorkstations.get(0);
        booking.setWorkstation(assignedWorkstation);

        // Retrieve the authenticated user (either standard user or OIDC user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails) {
            // Standard login case (from DB)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = credentialsService.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                    new IllegalStateException("Credentials not found for username: " + userDetails.getUsername())
            ).getUser();
            booking.setUser(currentUser);
            logger.info("Booking created by STANDARD user: {} {} {}", currentUser.getName(), currentUser.getSurname(), currentUser.getEmail());

        } else if (authentication.getPrincipal() instanceof OidcUser) {
            // OIDC login case (from OAuth provider)
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            // Fetch user by username (based on the email)
            User currentUser = credentialsService.findByUsername("USERNAMEof" + oidcUser.getEmail()).orElseThrow(() ->
                    new IllegalStateException("Credentials not found for OIDC user with username: USERNAMEof" + oidcUser.getEmail())
            ).getUser();

            booking.setUser(currentUser);
            logger.info("Booking created by OIDC user: {} {}", currentUser.getName(), currentUser.getSurname());

        } else {
            throw new IllegalStateException("Unable to determine the user type.");
        }

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved: {}", savedBooking);

        return savedBooking;
    }
}
