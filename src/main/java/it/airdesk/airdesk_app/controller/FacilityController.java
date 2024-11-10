package it.airdesk.airdesk_app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.service.FacilityService;

@Controller
public class FacilityController { //this class, actually just handles the starting of the Workstation Booking system operation

    @Autowired
    private FacilityService facilityService;
    
    @GetMapping("/searchFacilities")
    public String searchFacilities(@RequestParam("city") String city, Model model) {
        List<Facility> facilities = facilityService.findAllByCity(city);
        Map<Long, Integer> facility2workstations = facilityService.findWorkstationCountByFacility();
        model.addAttribute("facilities", facilities);
        model.addAttribute("city", city);
        model.addAttribute("facility2workstations", facility2workstations);
        return "forms/searchFacilities";
    }


}
