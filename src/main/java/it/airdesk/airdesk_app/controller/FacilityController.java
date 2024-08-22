package it.airdesk.airdesk_app.controller;

import org.hibernate.mapping.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.service.FacilityService;

@Controller
public class FacilityController {
    
    @GetMapping("/searchFacilities")
    public String searchFacilities(@RequestParam("city") String city, Model model) {
        List<Facility> facilities = FacilityService.findAllByCity(city);
        

    }
}
