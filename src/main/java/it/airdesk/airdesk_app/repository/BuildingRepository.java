package it.airdesk.airdesk_app.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Building;
@Repository
public interface BuildingRepository extends CrudRepository<Building, Long> {
    
    @Query(
        "SELECT b FROM Building b JOIN b.openingHours oh " +
        "WHERE b.facility.id = :facilityId AND oh.day = :day"
    )
    List<Building> findBuildingsOpenOnDate(Long facilityId, DayOfWeek day);
}
