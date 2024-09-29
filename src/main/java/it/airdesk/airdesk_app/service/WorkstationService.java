package it.airdesk.airdesk_app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Workstation;
import it.airdesk.airdesk_app.repository.WorkstationRepository;

@Service
public class WorkstationService {
        
    @Autowired
    private WorkstationRepository workstationRepository;

    public Workstation findById(Long id) {
        return workstationRepository.findById(id).orElse(null);
    }

    List<Workstation> findAvailableWorkstations(
        Long buildingId,
        String workstationType,
        LocalDate bookingDate,
        LocalTime startingTime,
        LocalTime endingTime,
        DayOfWeek dayOfWeek) {
        return this.workstationRepository.findAvailableWorkstations(
            buildingId,
            workstationType,
            bookingDate,
            startingTime,
            endingTime,
            dayOfWeek);
    }

    public void save(Workstation workstation) {
        this.workstationRepository.save(workstation);
    }

}
