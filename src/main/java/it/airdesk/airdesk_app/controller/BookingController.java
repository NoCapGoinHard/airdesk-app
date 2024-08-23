package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.service.BookingService;
import it.airdesk.airdesk_app.service.FacilityService;


@Controller
public class BookingController {
    
    @Autowired
    private FacilityService facilityService;

    @GetMapping("/bookingMenu/{facility_id}")
    public String bookingMenu(@PathVariable("facility_id") Long facilityId, Model model) {
        Facility facility = facilityService.findById(facilityId);
        model.addAttribute("facility", facility);
        return "forms/bookingMenu";
    }
    
}
