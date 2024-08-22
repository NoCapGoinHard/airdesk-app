package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Workstation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workstationId;   //common name in the office for employees

    private Room room;

    public static final String SEATING_AREA = "seating area with table";
    public static final String PC_WORKSTATION = "workstation with PC and internet connection";
    public static final String MEETING_ROOM = "area designated as a meeting room";    

    private String workstationType;

    private List<Booking> bookings = new ArrayList<>();

    public Workstation(){}

    /////////////       GETTERS+SETTERS       //////////////////////
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getWorkstationType() {
        return workstationType;
    }

    public void setWorkstationType(String workstationType) {
        this.workstationType = workstationType;
    }
    
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    /////////////       AUXILIARY METHODS       ////////////////////


    /////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((workstationId == null) ? 0 : workstationId.hashCode());
        result = prime * result + ((room == null) ? 0 : room.hashCode());
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
        Workstation other = (Workstation) obj;
        if (workstationId == null) {
            if (other.workstationId != null)
                return false;
        } else if (!workstationId.equals(other.workstationId))
            return false;
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        return true;
    }

    
}
