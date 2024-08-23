package it.airdesk.airdesk_app.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
    
    @Autowired
    private FacilityService facilityService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/bookingMenu/{facility_id}")
    public String bookingMenu(@PathVariable("facility_id") Long facilityId, Model model) {
        Facility facility = facilityService.findById(facilityId);
        model.addAttribute("booking", new Booking());
        model.addAttribute("facility", facility);
        return "forms/bookingMenu";
    }

    @PostMapping("/bookingMenu/filter")
    public String filterBuildingsByDate(
        @RequestParam Long facilityId,
        @RequestParam LocalDate bookingDate,
        Model model) {
        DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();

        List<Building> openBuildings = buildingRepository.findBuildingsOpenOnDate(facilityId, dayOfWeek);
        model.addAttribute("openBuildings", openBuildings);
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("facility_id", facilityId);

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
        try {
            booking.setDate(bookingDate);
            Booking confirmedBooking = bookingService.createBooking(booking, buildingId, workstationType);
            model.addAttribute("confirmedBooking", confirmedBooking);
            return "forms/bookingReceipt";
        } catch (NoAvailableWorkstationException exc) {
            model.addAttribute("error", exc.getMessage());
            model.addAttribute("booking", booking);
            model.addAttribute("facility_id", facilityId);
            model.addAttribute("building_id", buildingId);

            DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
            List<Building> openBuildings = buildingRepository.findBuildingsOpenOnDate(facilityId, dayOfWeek);
            model.addAttribute("openBuildings", openBuildings);
            return "forms/bookingMenu";
        }
    }
    
    
}
