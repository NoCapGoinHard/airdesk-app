package it.airdesk.airdesk_app.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import it.airdesk.airdesk_app.service.BuildingService;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.FacilityService;
import it.airdesk.airdesk_app.service.RoomService;
import it.airdesk.airdesk_app.service.WorkstationService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
@RequestMapping("/admin")  //EVERY ENDPOINT WILL START WITH '/admin/rest of it'
public class AdminController {
    
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private BuildingService buildingService;

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
        return "admin/createFacility";
    }

    // Save the New Facility
    @PostMapping("/newFacility")
    public String newFacility(@ModelAttribute Facility facility,
                                  @RequestParam(value = "newCompany", required = false) String newCompanyName,
                                  Model model) {
        // Handle company if a new one was added
        if (newCompanyName != null && !newCompanyName.isEmpty()) {
            Company newCompany = companyService.findOrCreateCompanyByName(newCompanyName);
            facility.addCompany(newCompany);
        }

        // Save buildings, floors, and rooms, similar to updateFacility method
        saveBuildings(facility);

        // Save the new facility
        facilityService.save(facility);

        return "redirect:/admin/manageFacilities";  // Redirect back to manage facilities page
    }

    private void saveBuildings(Facility facility) {
        for (Building building : facility.getBuildings()) {
            building.setFacility(facility);  // Link building to facility
            buildingService.save(building);

            for (Floor floor : building.getFloors()) {
                floor.setBuilding(building);  // Link floor to building

                for (Room room : floor.getRooms()) {
                    room.setFloor(floor);  // Link room to floor
                    roomService.save(room);

                    for (Workstation workstation : room.getWorkstations()) {
                        workstation.setRoom(room);  // Link workstation to room
                        workstationService.save(workstation);
                    }
                }
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Edit facility - Display the edit form
    @GetMapping("/editFacility/{id}")
    public String editFacility(@PathVariable Long id, Model model) {
        Facility facility = facilityService.findById(id);
    
        // Initialize buildings, floors, rooms, and workstations if null
        if (facility.getBuildings() == null) {
            facility.setBuildings(new ArrayList<>());
        }
    
        for (Building building : facility.getBuildings()) {
            if (building.getFloors() == null) {
                building.setFloors(new ArrayList<>());
            }
            for (Floor floor : building.getFloors()) {
                if (floor.getRooms() == null) {
                    floor.setRooms(new ArrayList<>());
                }
                for (Room room : floor.getRooms()) {
                    if (room.getWorkstations() == null) {
                        room.setWorkstations(new ArrayList<>());
                    }
                }
            }
        }
    
        model.addAttribute("facility", facility);
        return "admin/editFacility";
    }

    @PostMapping("/updateFacility/{id}")
    public String updateFacility(@PathVariable Long id, @ModelAttribute Facility updatedFacility,
                                 @RequestParam(value = "newCompany", required = false) String newCompanyName, Model model) {
        Facility facility = facilityService.findById(id);

        // Update basic facility details
        facility.setName(updatedFacility.getName());
        facility.setPhone(updatedFacility.getPhone());
        facility.setEmail(updatedFacility.getEmail());

        // Handle adding a new company
        if (newCompanyName != null && !newCompanyName.isEmpty()) {
            Company newCompany = companyService.findOrCreateCompanyByName(newCompanyName);
            facility.addCompany(newCompany);
        }

        // Update buildings, floors, and rooms properly
        updateBuildings(facility, updatedFacility);

        // Save updated facility
        facilityService.save(facility);
        return "redirect:/admin/editFacility/" + id;
    }

    private void updateBuildings(Facility facility, Facility updatedFacility) {
        for (Building updatedBuilding : updatedFacility.getBuildings()) {
            Building building = buildingService.findById(updatedBuilding.getId());
            
            if (building == null) {
                building = new Building();
                facility.addBuilding(building);
            }

            // Update building details
            building.setName(updatedBuilding.getName());
            building.setAddress(updatedBuilding.getAddress());
            building.setOpeningHours(updatedBuilding.getOpeningHours());

            // Update floors associated with the building
            updateFloors(building, updatedBuilding);
            buildingService.save(building); // Save the building after updating the floors
        }
    }

    private void updateFloors(Building building, Building updatedBuilding) {
        for (Floor updatedFloor : updatedBuilding.getFloors()) {
            Floor floor = updatedFloor.getId() != null ? building.getFloors().stream()
                    .filter(f -> f.getId().equals(updatedFloor.getId()))
                    .findFirst().orElse(null) : null;

            if (floor == null) {
                // Create a new floor and associate it with the building
                floor = new Floor();
                building.addFloor(floor);
            }

            // Update the floor details
            floor.setNumber(updatedFloor.getNumber());
            floor.setBuilding(building); // Link the floor to the building

            // Update rooms for each floor
            updateRooms(floor, updatedFloor);
        }
    }

    private void updateRooms(Floor floor, Floor updatedFloor) {
        for (Room updatedRoom : updatedFloor.getRooms()) {
            Room room = updatedRoom.getId() != null ? floor.getRooms().stream()
                    .filter(r -> r.getId().equals(updatedRoom.getId()))
                    .findFirst().orElse(null) : null;

            if (room == null) {
                // Create a new room and associate it with the floor
                room = new Room();
                floor.addRoom(room);
            }

            // Update the room details
            room.setName(updatedRoom.getName());
            room.setOpeningHours(updatedRoom.getOpeningHours());
            room.setFloor(floor); // Link the room to the floor

            // Update workstations for each room
            updateWorkstations(room, updatedRoom);
        }
    }

    private void updateWorkstations(Room room, Room updatedRoom) {
        for (Workstation updatedWorkstation : updatedRoom.getWorkstations()) {
            Workstation workstation = updatedWorkstation.getId() != null ? room.getWorkstations().stream()
                    .filter(w -> w.getId().equals(updatedWorkstation.getId()))
                    .findFirst().orElse(null) : null;

            if (workstation == null) {
                // Create a new workstation and associate it with the room
                workstation = new Workstation();
                room.addWorkstation(workstation);
            }

            // Update workstation details
            workstation.setWorkstationId(updatedWorkstation.getWorkstationId());
            workstation.setWorkstationType(updatedWorkstation.getWorkstationType());
            workstation.setRoom(room); // Link the workstation to the room
            workstationService.save(workstation); // Save the workstation
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Delete facility
    @PostMapping("/admin/deleteFacility/{id}")
    public String deleteFacility(@PathVariable Long id) {
        facilityService.deleteById(id);
        return "redirect:/admin/manageFacilities";
    }
}
