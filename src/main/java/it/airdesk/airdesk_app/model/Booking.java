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

    private User user;
}
