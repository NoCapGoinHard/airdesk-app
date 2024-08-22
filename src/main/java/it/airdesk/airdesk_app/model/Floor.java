package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Floor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "number field must not be null")
    @Column(nullable = false)
    private Integer number;

    @NotNull(message = "building field must not be null")
    @Column(nullable = false)
    private Building building;

    @NotNull(message = "rooms field must not be null")
    @Column(nullable = false)
    private List<Room> rooms = new ArrayList<>();

    public Floor(){}

    /////////////       GETTERS+SETTERS       //////////////////////
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNumber() {
        return number;
    }
    
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public Building getBuilding() {
        return building;
    }
    
    public void setBuilding(Building building) {
        this.building = building;
    }
    
    public List<Room> getRooms() {
        return rooms;
    }
    
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    /////////////       AUXILIARY METHODS       ////////////////////
    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    /////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((building == null) ? 0 : building.hashCode());
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
        Floor other = (Floor) obj;
        if (number == null) {
            if (other.number != null)
            return false;
        } else if (!number.equals(other.number))
        return false;
        if (building == null) {
            if (other.building != null)
                return false;
        } else if (!building.equals(other.building))
            return false;
        return true;
    }

    
}
