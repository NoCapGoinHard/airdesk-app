package it.airdesk.airdesk_app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WorkstationRepository workstationRepository;

    @Transactional
    public Booking createBooking(Booking booking, Long buildingId, String workstationType) throws NoAvailableWorkstationException { 

        logger.info("Creating booking for building ID: {}, workstation type: {}, date: {}, start time: {}, end time: {}",
                    buildingId, workstationType, booking.getDate(), booking.getStartingTime(), booking.getEndingTime()
                    );

        List<Workstation> availableWorkstations = workstationRepository.findAvailableWorkstations(
            buildingId,
            workstationType,
            booking.getDate(),
            booking.getStartingTime(),
            booking.getEndingTime()
        );

        if(availableWorkstations.isEmpty()) {
            logger.warn("No available workstations found for building ID: {}, workstation type: {}, date: {}, start time: {}, end time: {}",
                    buildingId, workstationType, booking.getDate(), booking.getStartingTime(), booking.getEndingTime()
                    );
            throw new NoAvailableWorkstationException("No available workstations for the selected parameters.");
        }

        Workstation assignedWorkstation = availableWorkstations.get(0);
        booking.setWorkstation(assignedWorkstation);
        logger.debug("Assigned workstation: {}", assignedWorkstation);

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved: {}", savedBooking);
        
        return savedBooking;
    }
}
