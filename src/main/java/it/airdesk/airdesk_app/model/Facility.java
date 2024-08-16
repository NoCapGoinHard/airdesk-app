package it.airdesk.airdesk_app.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Facility {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String phone;

    @NotBlank
    private String email;

    private Map<DayOfWeek, List<OfficeHours>> openingHours = new HashMap<>(); //value is a list in order to handle duplicate opening hours if a building closes at lunch and reopens at noon

    private List<Building> buildings = new ArrayList<>();

    List<User> authorizedAdministrators = new ArrayList<>();

    public Facility(){}
    
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<DayOfWeek, List<OfficeHours>> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<DayOfWeek, List<OfficeHours>> openingTimes) {
        this.openingHours = openingHours;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<User> getAuthorizedAdministrators() {
        return authorizedAdministrators;
    }

    public void setAuthorizedAdministrators(List<User> authorizedAdministrators) {
        this.authorizedAdministrators = authorizedAdministrators;
    }

    /////////////       AUXILIARY METHODS       ////////////////////

    public void updateOpeningHours(DayOfWeek day, List<OfficeHours> officeHours) {
        this.openingHours.put(day, officeHours);
    }

    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public void addAuthorizedAdministrator(User user) throws Exception{
        if(user.getRole == "ADMIN") this.authorizedAdministrators.add(user);
        else throw new Exception(
            "eccezione dentro classe Facility, metodo addAuthorizedAdministrator, non puoi aggiungerlo"
            );
    }


    /////////////       HashCode + equals METHODS       ////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
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
        Facility other = (Facility) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }
    

    

}
