package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "name field must not be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "facilities field must not be null")
    @Column(nullable = false)
    private List<Facility> facilities = new ArrayList<>();

    public Company(){}
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

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    /////////////       AUXILIARY METHODS       ////////////////////
    public void addFacility(Facility facility) {
        this.facilities.add(facility);
    }
    /////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((facilities == null) ? 0 : facilities.hashCode());
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
        Company other = (Company) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (facilities == null) {
            if (other.facilities != null)
                return false;
        } else if (!facilities.equals(other.facilities))
            return false;
        return true;
    }
    
    
}
