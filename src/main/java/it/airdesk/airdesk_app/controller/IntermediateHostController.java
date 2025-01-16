package it.airdesk.airdesk_app.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
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
import org.springframework.web.multipart.MultipartFile;

import it.airdesk.airdesk_app.model.Building;
import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.Employee;
import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.model.Floor;
import it.airdesk.airdesk_app.model.Room;
import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.model.auth.IntermediateHost;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import it.airdesk.airdesk_app.service.BuildingService;
import it.airdesk.airdesk_app.service.CompanyService;
import it.airdesk.airdesk_app.service.EmployeeService;
import it.airdesk.airdesk_app.service.FacilityService;
import it.airdesk.airdesk_app.service.FloorService;
import it.airdesk.airdesk_app.service.OfficeHoursService;
import it.airdesk.airdesk_app.service.RoomService;
import it.airdesk.airdesk_app.service.WorkstationService;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@Controller
@RequestMapping("/intermediateHost")
public class IntermediateHostController { //class which handles the Host's reserved operations

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
    
    @Autowired
    private EmployeeService employeeService; // Servizio per salvare i dipendenti nel database

    // Render the Host Dashboard page
    @GetMapping("/dashboard")
    public String getIntermediateHostDashboard(Model model) {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

        if (credentials != null && credentials.getRole().equals(Credentials.INTERMEDIATE_HOST)) {
            // Host is authenticated, show the dashboard
        	IntermediateHost intermediateHost = credentials.getIntermediateHost();
            model.addAttribute("intermediateHost", intermediateHost);  // Add user to the model for display
            return "intermediateHost/intermediateHostDashboard.html";
        }

        return "redirect:/login";  // Redirect to login if not authenticated
    }
    
    // Route to upload an Excel file to register the employees
    @GetMapping("/uploadExcelFile")
    public String uploadExcelFile() {        
        return "intermediateHost/uploadExcelFile.html";
    }
    
    // Endpoint per gestire l'upload del file Excel.
    @PostMapping("/uploadExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream); // Usa XSSFWorkbook per .xlsx, HSSFWorkbook per .xls
            Sheet sheet = (Sheet) workbook.getSheetAt(0); // Prende il primo foglio

            Iterator<Row> rows = sheet.iterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                if (row.getRowNum() == 0) {
                    continue; // Salta la prima riga (header)
                }

                // Estrai i dati delle celle (nome, cognome, email)
                String firstName = row.getCell(0).getStringCellValue(); // Prima colonna
                String lastName = row.getCell(1).getStringCellValue(); // Seconda colonna
                String email = row.getCell(2).getStringCellValue(); // Terza colonna

                // Crea un oggetto Employee (modello) e salvalo nel database
                Employee employee = new Employee(firstName, lastName, email);
                employeeService.save(employee); // Salva nel database
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Ritorna una pagina di errore in caso di problemi
        }
        return "redirect:/intermediateHost/dashboard"; // Redirect al dashboard dopo l'upload
    }

    // Route to manage facilities (shows the list of facilities)
    @GetMapping("/manageFacilities")
    public String manageFacilities(Model model) {
        // Get the authenticated host's credentials
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
        
        if (credentials == null || !credentials.isIntermediateHost()) {
            return "redirect:/login";
        }

        // Fetch the facilities for the admin's company
        Long companyId = credentials.getUser().getCompany().getId();
        model.addAttribute("facilities", facilityService.findByCompanyId(companyId));

        return "host/manageFacilities";
    }

    // View facility details
    @GetMapping("/facility/{id}")
    public String facilityPage(@PathVariable Long id, Model model) {
        Facility facility = facilityService.findById(id);
        model.addAttribute("facility", facility);
        return "host/facilityPage";
    }

    @GetMapping("/newFacility")
    public String newFacility(Model model) {
        model.addAttribute("facility", new Facility());  // Empty facility object for form binding
        return "host/newFacility";
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

        return "redirect:/host/facilityPage/" + facility.getId();
//        return "/host/hostDashboard.html";
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
    @PostMapping("/host/deleteFacility/{id}")
    public String deleteFacility(@PathVariable Long id) {
        facilityService.deleteById(id);
        return "redirect:/host/manageFacilities";
    }
}