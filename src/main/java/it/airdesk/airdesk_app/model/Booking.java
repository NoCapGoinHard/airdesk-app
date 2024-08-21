package it.airdesk.airdesk_app.model;

import java.time.LocalDate;

import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate day;

    @NotNull
    @Embedded
    private OfficeHours bookingPeriod;

    private Workstation workstation;

    private User user;

    public Booking(){}

    /////////////       GETTERS+SETTERS       //////////////////////
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public OfficeHours getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(OfficeHours bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    /////////////       AUXILIARY METHODS       ////////////////////

    
    /////////////       HashCode + equals METHODS       ////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + ((bookingPeriod == null) ? 0 : bookingPeriod.hashCode());
        result = prime * result + ((workstation == null) ? 0 : workstation.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Booking other = (Booking) obj;
        if (day == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
            return false;
        if (bookingPeriod == null) {
            if (other.bookingPeriod != null)
                return false;
        } else if (!bookingPeriod.equals(other.bookingPeriod))
            return false;
        if (workstation == null) {
            if (other.workstation != null)
                return false;
        } else if (!workstation.equals(other.workstation))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


    
}
