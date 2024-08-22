package it.airdesk.airdesk_app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.Column;
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

    @NotNull(message = "day field must not be null")
    @Column(nullable = false)
    private LocalDate day;

    @NotNull(message = "starting time field must not be null")
    @Column(nullable = false)
    private LocalTime startingTime;

    @NotNull(message = "ending time field must not be null")
    @Column(nullable = false)
    private LocalTime endingTime;

    @NotNull(message = "workstation field must not be null")
    @Column(nullable = false)
    private Workstation workstation;

    @NotNull(message = "user must not be null")
    @Column(nullable = false)
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
    
    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
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
        result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
        result = prime * result + ((endingTime == null) ? 0 : endingTime.hashCode());
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
            if (startingTime == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
            return false;
            if (other.startingTime != null)
                return false;
        } else if (!startingTime.equals(other.startingTime))
            return false;
        if (endingTime == null) {
            if (other.endingTime != null)
                return false;
        } else if (!endingTime.equals(other.endingTime))
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
