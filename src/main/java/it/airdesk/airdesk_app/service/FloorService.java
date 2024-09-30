package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Floor;
import it.airdesk.airdesk_app.repository.FloorRepository;

@Service
public class FloorService {
        
    @Autowired
    private FloorRepository floorRepository;

    public void save(Floor floor) {
        this.floorRepository.save(floor);
    }
}
