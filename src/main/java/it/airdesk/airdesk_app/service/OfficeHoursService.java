package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import it.airdesk.airdesk_app.repository.OfficeHoursRepository;

@Service
public class OfficeHoursService {

    @Autowired
    private OfficeHoursRepository officeHoursRepository;
    
    public void save(OfficeHours openingHours) {
        this.officeHoursRepository.save(openingHours);
    }
}
