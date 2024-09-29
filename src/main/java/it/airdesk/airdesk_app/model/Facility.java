package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Facility {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name field must not be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "phone field must not be blank")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "email field must not be blank")
    @Column(nullable = false)
    private String email;

    @NotNull(message = "buildings field must not be null")
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings = new ArrayList<>();

    @NotNull(message = "companies field must not be null")
    @ManyToMany(mappedBy = "facilities")
    private List<Company> companies = new ArrayList<>();

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

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    /////////////       AUXILIARY METHODS       ////////////////////

    public void addBuilding(Building building) {
        this.buildings.add(building);
    }
    public void removeBuilding(Building building) {
        this.buildings.remove(building);
    }

    public void addCompany(Company company) {
        this.companies.add(company);
    }
    public void removeCompany(Company company) {
        this.companies.remove(company);
    }

    /////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((buildings == null) ? 0 : buildings.hashCode());
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
        if (buildings == null) {
            if (other.buildings != null)
                return false;
        } else if (!buildings.equals(other.buildings))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder companiesNames = new StringBuilder();
        for (Company company : companies) {
            companiesNames.append(company.getName()).append(", ");
        }
        // Remove the trailing comma and space if any companies were added
        if (companiesNames.length() > 0) {
            companiesNames.setLength(companiesNames.length() - 2);
        }

        return "Facility [name: " + name + ", companies: " + companiesNames.toString() + "]";
    }


    

    

}
