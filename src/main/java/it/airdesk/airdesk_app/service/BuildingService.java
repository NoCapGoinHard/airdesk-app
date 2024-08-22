package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.repository.BuildingRepository;

@Service
public class BuildingService {
        
    @Autowired
    private BuildingRepository buildingRepository;
}
