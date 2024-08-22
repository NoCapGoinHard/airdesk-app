package it.airdesk.airdesk_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Booking;
@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    
}
