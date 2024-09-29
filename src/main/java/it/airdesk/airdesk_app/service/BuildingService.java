package it.airdesk.airdesk_app.service;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Building;
import it.airdesk.airdesk_app.repository.BuildingRepository;

@Service
public class BuildingService {
        
    @Autowired
    private BuildingRepository buildingRepository;

    public Building findById(Long id) {
        return this.buildingRepository.findById(id).orElse(null);
    }

    public List<Building> findBuildingsOpenOnDate(Long facilityId, DayOfWeek day) {
        return buildingRepository.findBuildingsOpenOnDate(facilityId, day);
    }

    public void save(Building building) {
        buildingRepository.save(building);
    }
}
