package it.airdesk.airdesk_app.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Workstation;
@Repository
public interface WorkstationRepository extends CrudRepository<Workstation, Long> {
    
    @Query(
        "SELECT w FROM Workstation w WHERE w.room.floor.building.id =:buildingId" +
        "AND w.workstationType = :workstationType" +
        "AND w.id NOT IN (" +
            "SELECT b.workstation.id FROM Booking b" +
            "WHERE b.day =:day" +
            "AND (" +
            "(:startingTime < b.endingTime AND :end > b.startingTime)" +
            ")" +
        ")"
    )
    List<Workstation> findAvailableWorkstations(Long buildingId, String workstationType, LocalDate day, LocalTime startingTime, LocalTime endingTime);
}
