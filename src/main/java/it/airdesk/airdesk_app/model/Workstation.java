package it.airdesk.airdesk_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class Workstation {

    public static final String SEATING_AREA = "seating area with table";
    public static final String PC_WORKSTATION = "workstation with PC and internet connection";
    public static final String MEETING_ROOM = "area designated as a meeting room"; 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "workstationId field must not be blank")
    @Column(nullable = false)
    private String workstationId;   //FAR DIFFERENT FROM THE Database ID, this is the workstation name in the actual workplace

    @NotNull(message = "room field must not be null")
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

   
    @NotBlank(message = "workstation type field must not be blank")
    @Column(nullable = false)
    private String workstationType;

    @NotNull(message = "bookings field must not be null")
    @OneToMany(mappedBy = "workstation", cascade = CascadeType.ALL, orphanRemoval = true)
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
    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
    }
    
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
