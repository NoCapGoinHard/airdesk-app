package it.airdesk.airdesk_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Building;
@Repository
public interface BuildingRepository extends CrudRepository<Building, Long> {
    
}
