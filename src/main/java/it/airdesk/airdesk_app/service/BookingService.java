package it.airdesk.airdesk_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.exceptions.NoAvailableWorkstationException;
import it.airdesk.airdesk_app.model.Booking;
import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.repository.BookingRepository;
import it.airdesk.airdesk_app.repository.WorkstationRepository;
import jakarta.transaction.Transactional;

@Service
public class BookingService {
        
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WorkstationRepository workstationRepository;

    @Transactional
    public Booking createBooking(Booking booking, Long buildingId, String workstationType) throws NoAvailableWorkstationException {
        List<Workstation> availableWorkstations = workstationRepository.findAvailableWorkstations(
            buildingId,
            workstationType,
            booking.getDate(),
            booking.getStartingTime(),
            booking.getEndingTime()
        );

        if(availableWorkstations.isEmpty()) {
            throw new NoAvailableWorkstationException("No available workstations for the selected parameters.");
        }

        booking.setWorkstation(availableWorkstations.get(0));
        return bookingRepository.save(booking);
    }
}
