package it.airdesk.airdesk_app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import it.airdesk.airdesk_app.model.auth.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "date field must not be null")
    @Column(nullable = false)
    @FutureOrPresent(message = "date field must be today or a future one")
    private LocalDate date;

    @NotNull(message = "starting time field must not be null")
    @Column(nullable = false)
    private LocalTime startingTime;

    @NotNull(message = "ending time field must not be null")
    @Column(nullable = false)
    private LocalTime endingTime;

    @NotNull(message = "workstation field must not be null")
    @ManyToOne
    @JoinColumn(name = "workstation_id", nullable = false)
    private Workstation workstation;

    @NotNull(message = "user must not be null")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Booking(){}

    /////////////       GETTERS+SETTERS       //////////////////////
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
        result = prime * result + ((endingTime == null) ? 0 : endingTime.hashCode());
        result = prime * result + ((workstation == null) ? 0 : workstation.hashCode());
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
        if (date == null) {
            if (startingTime == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
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
//        if (user == null) {
//            if (other.user != null)
//                return false;
//        } else if (!user.equals(other.user))
//            return false;
        return true;
    }

    




    
}
