package it.airdesk.airdesk_app.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Building;
@Repository
public interface BuildingRepository extends CrudRepository<Building, Long> {
    
    static final Logger logger = LoggerFactory.getLogger(BuildingRepository.class);
    @Query(
        "SELECT b FROM Building b JOIN b.openingHours oh " +
        "WHERE b.facility.id = :facilityId AND oh.day = :day"
    )

    public List<Building> findBuildingsOpenOnDate(Long facilityId, DayOfWeek day);

    default List<Building> findBuildingsOpenOnDateWithLogging(Long facilityId, DayOfWeek day) {
        logger.info("Executing query to find buildings open on date: {} for facility ID: {}", day, facilityId);
        return findBuildingsOpenOnDate(facilityId, day);
    }
}
