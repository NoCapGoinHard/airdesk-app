package it.airdesk.airdesk_app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.repository.FacilityRepository;

@Service
public class FacilityService {
        
    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> findAllByCity(String city) {
        return facilityRepository.findByAddress_CityContainingIgnoreCase(city);
    }
}
