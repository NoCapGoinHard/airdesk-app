package it.airdesk.airdesk_app.model.dataTypes;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class OfficeHours {

    @NotNull(message = "starting time field must not be null")
    @Column(nullable = false)
    private LocalTime startingTime;
    
    @NotNull(message = "ending time field must not be null")
    @Column(nullable = false)
    private LocalTime endingTime;

    public OfficeHours(){}
    
    public OfficeHours(LocalTime startingTime, LocalTime endingTime) {
        this.startingTime = startingTime;
        this.endingTime = endingTime;
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

/////////////       AUXILIARY METHODS       /////////////////////


/////////////       HashCode + equals METHODS       ////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
        result = prime * result + ((endingTime == null) ? 0 : endingTime.hashCode());
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
        OfficeHours other = (OfficeHours) obj;
        if (startingTime == null) {
            if (other.startingTime != null)
                return false;
        } else if (!startingTime.equals(other.startingTime))
            return false;
        if (endingTime == null) {
            if (other.endingTime != null)
                return false;
        } else if (!endingTime.equals(other.endingTime))
            return false;
        return true;
    }

    
}
