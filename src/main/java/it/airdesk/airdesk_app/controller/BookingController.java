package it.airdesk.airdesk_app.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.exceptions.NoAvailableWorkstationException;
import it.airdesk.airdesk_app.model.Booking;
import it.airdesk.airdesk_app.model.Building;
import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.repository.BuildingRepository;
import it.airdesk.airdesk_app.service.BookingService;
import it.airdesk.airdesk_app.service.FacilityService;



@Controller
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private FacilityService facilityService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/bookingMenu/{facility_id}")
    public String bookingMenu(@PathVariable("facility_id") Long facilityId, Model model) {
        logger.info("Loading booking menu for facility ID: {}", facilityId);
        Facility facility = facilityService.findById(facilityId);
        logger.debug("Facility found: {}", facility);
        model.addAttribute("booking", new Booking());
        model.addAttribute("facility", facility);
        return "forms/bookingMenu";
    }

    @PostMapping("/bookingMenu/filter")
    public String filterBuildingsByDate(
        @RequestParam("facilityId") Long facilityId,
        @RequestParam("bookingDate") LocalDate bookingDate,
        Model model) {
        logger.info("Filtering buildings for facility ID: {} on date: {}", facilityId, bookingDate);

        DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
        List<Building> openBuildings = buildingRepository.findBuildingsOpenOnDate(facilityId, dayOfWeek);

        // Check for null or empty list
        if (openBuildings == null || openBuildings.isEmpty()) {
            model.addAttribute("error", "No buildings are open on the selected date.");
            return "forms/bookingMenu";
        }
        for (Iterator<Building> iterator = openBuildings.iterator(); iterator.hasNext(); ) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }
        logger.debug("Open buildings found: {}", openBuildings);
        model.addAttribute("openBuildings", openBuildings);
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("facility_id", facilityId);
        Facility facility = facilityService.findById(facilityId);
        model.addAttribute("facility", facility);

        return "forms/bookingMenu";
    }

    @PostMapping("/bookWorkstation")
    public String bookWorkstation(
            @ModelAttribute Booking booking,
            @RequestParam Long facilityId,
            @RequestParam Long buildingId,
            @RequestParam String workstationType,
            @RequestParam LocalDate bookingDate,
            Model model) {
            
            Facility facility = facilityService.findById(facilityId);
        try {
            model.addAttribute("facility", facility);
            logger.info("Attempting to book workstation for facility ID: {}, building ID: {}, date: {}, type: {}", facilityId, buildingId, bookingDate, workstationType);
            booking.setDate(bookingDate);
            Booking confirmedBooking = bookingService.createBooking(booking, buildingId, workstationType);
            logger.info("Booking successful: {}", confirmedBooking);
            model.addAttribute("confirmedBooking", confirmedBooking);
            logger.info("Building ID: {}", buildingId);
            logger.info("Workstation Type: {}", workstationType);
            logger.info("Date: {}", booking.getDate());
            logger.info("Start Time: {}", booking.getStartingTime());
            logger.info("End Time: {}", booking.getEndingTime());

            return "redirect:/bookingReceipt/" + confirmedBooking.getId();

        } catch (NoAvailableWorkstationException exc) {
            model.addAttribute("facility", facility);
            logger.error("No available workstation for facility ID: {}, building ID: {}, date: {}, type: {}. Error: {}", facilityId, buildingId, bookingDate, workstationType, exc.getMessage());
            logger.info("Building ID: {}", buildingId);
            logger.info("Workstation Type: {}", workstationType);
            logger.info("Date: {}", booking.getDate());
            logger.info("Start Time: {}", booking.getStartingTime());
            logger.info("End Time: {}", booking.getEndingTime());
            model.addAttribute("error", exc.getMessage());
            model.addAttribute("booking", booking);
            model.addAttribute("facility_id", facilityId);
            model.addAttribute("building_id", buildingId);

            DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
            List<Building> openBuildings = buildingRepository.findBuildingsOpenOnDateWithLogging(facilityId, dayOfWeek);
            logger.debug("Reloading open buildings: {}", openBuildings);
            model.addAttribute("openBuildings", openBuildings);
            return "forms/bookingMenu";
        }
    }
    
    
}
