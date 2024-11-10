package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.airdesk.airdesk_app.model.Building;
import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.model.Floor;
import it.airdesk.airdesk_app.model.Room;
import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import it.airdesk.airdesk_app.service.BuildingService;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.FacilityService;
import it.airdesk.airdesk_app.service.FloorService;
import it.airdesk.airdesk_app.service.OfficeHoursService;
import it.airdesk.airdesk_app.service.RoomService;
import it.airdesk.airdesk_app.service.WorkstationService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
@RequestMapping("/admin")
public class AdminController { //class which handles the Administrator's reserved operations

    @Autowired
    private CredentialsService credentialsService;
    
    @Autowired
    private CompanyService companyService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private BuildingService buildingService;
    
    @Autowired
    private OfficeHoursService officeHoursService;

    @Autowired
    private FloorService floorService;
    
    @Autowired
    private RoomService roomService;

    @Autowired
    private WorkstationService workstationService;


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

    @GetMapping("/newFacility")
    public String newFacility(Model model) {
        model.addAttribute("facility", new Facility());  // Empty facility object for form binding
        return "admin/newFacility";
    }

    // Save the New Facility
    @PostMapping("/newFacility")
    @Transactional
    public String newFacility(@ModelAttribute Facility facility,
                              @RequestParam(value = "newCompany", required = false) String newCompanyName) {
        // Handle company if a new one was added
        if (newCompanyName != null && !newCompanyName.isEmpty()) {
            Company newCompany = companyService.findOrCreateCompanyByName(newCompanyName);
            facility.addCompany(newCompany);
        }

        // Save the facility first to generate its ID
        facilityService.save(facility);

        // Now save buildings, floors, rooms, and workstations
        saveBuildingsAndRelatedEntities(facility);

        return "redirect:/admin/facilityPage/" + facility.getId();
    }

    private void saveBuildingsAndRelatedEntities(Facility facility) {
        for (Building building : facility.getBuildings()) {
            // Set facility for each building and add it
            building.setFacility(facility);
            facility.addBuilding(building);

            buildingService.save(building);

            // Save building opening hours
            for (OfficeHours openingHours : building.getOpeningHours()) {
                officeHoursService.save(openingHours);
            }

            // Now save floors for each building
            saveFloors(building);
        }
    }

    private void saveFloors(Building building) {
        for (Floor floor : building.getFloors()) {
            floor.setBuilding(building);  // Link floor to building
            building.addFloor(floor);  // Add to building's floors collection
            floorService.save(floor);  // Save floor

            // Save rooms for each floor
            saveRooms(floor);
        }
    }

    private void saveRooms(Floor floor) {
        for (Room room : floor.getRooms()) {
            room.setFloor(floor);  // Link room to floor
            floor.addRoom(room);  // Add to floor's rooms collection
            roomService.save(room);  // Save room

            // Save room opening hours
            for (OfficeHours openingHours : room.getOpeningHours()) {
                officeHoursService.save(openingHours);
            }

            // Save workstations for each room
            saveWorkstations(room);
        }
    }

    private void saveWorkstations(Room room) {
        for (Workstation workstation : room.getWorkstations()) {
            workstation.setRoom(room);  // Link workstation to room
            room.addWorkstation(workstation);  // Add to room's workstations collection
            workstationService.save(workstation);  // Save workstation
        }
    }

    // Delete facility
    @PostMapping("/admin/deleteFacility/{id}")
    public String deleteFacility(@PathVariable Long id) {
        facilityService.deleteById(id);
        return "redirect:/admin/manageFacilities";
    }
}