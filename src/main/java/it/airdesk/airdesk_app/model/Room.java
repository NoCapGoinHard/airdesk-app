package it.airdesk.airdesk_app.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name field must not be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "floor field must not be null")
    @Column(nullable = false)
    private Floor floor;

    @NotNull(message = "opening hours must not be null")
    @Column(nullable = false)
    private Map<DayOfWeek, List<OfficeHours>> openingHours = new HashMap<>();

    @NotNull(message = "workstations field must not be null")
    @Column(nullable = false)
    private List<Workstation> workstations = new ArrayList<>();


    public Room(){}
    /////////////       GETTERS+SETTERS       //////////////////////
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Floor getFloor() {
        return floor;
    }
    
    public void setFloor(Floor floor) {
        this.floor = floor;
    }
    
    public Map<DayOfWeek, List<OfficeHours>> getOpeningHours() {
        return openingHours;
    }
    
    public void setOpeningHours(Map<DayOfWeek, List<OfficeHours>> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Workstation> getWorkstations() {
        return workstations;
    }

    public void setWorkstations(List<Workstation> workstations) {
        this.workstations = workstations;
    }


    /////////////       AUXILIARY METHODS       ////////////////////
    public void updateOpeningHours(DayOfWeek day, List<OfficeHours> officeHours) {
        this.openingHours.put(day, officeHours);
    }

    public void addWorkstation(Workstation workstation) {
        this.workstations.add(workstation);
    }


    /////////////       HashCode + equals METHODS       ////////////
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((floor == null) ? 0 : floor.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        return true;
        if (obj == null)
        return false;
        if (getClass() != obj.getClass())
        return false;
        Room other = (Room) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (floor == null) {
            if (other.floor != null)
                return false;
        } else if (!floor.equals(other.floor))
            return false;
        return true;
    }

    
}
