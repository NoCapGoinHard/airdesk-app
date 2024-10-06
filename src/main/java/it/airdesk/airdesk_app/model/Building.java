package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import it.airdesk.airdesk_app.model.dataTypes.Address;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Building {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name field must not be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "address field must not be null")
    @Embedded
    private Address address;

    @NotNull(message = "facility field must not be null")
    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @NotNull(message = "opening hours must not be null")
    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "building_id")
    private List<OfficeHours> openingHours = new ArrayList<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();

    public Building(){}

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<OfficeHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OfficeHours> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }


    /////////////       AUXILIARY METHODS       ////////////////////
    public void addFloor(Floor floor) {
        this.floors.add(floor);
    }

    public void addOpeningHours(OfficeHours openingHours) {
        this.openingHours.add(openingHours);
    }

    public void removeOpeningHours(OfficeHours openingHours) {
        this.openingHours.remove(openingHours);
    }
    
    /////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
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
        Building other = (Building) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }

    
}
