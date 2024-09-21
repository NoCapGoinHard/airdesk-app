package it.airdesk.airdesk_app.repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Workstation;
@Repository
public interface WorkstationRepository extends CrudRepository<Workstation, Long> {
    
    @Query(
        "SELECT w FROM Workstation w " +
        "JOIN w.room r " +
        "JOIN r.openingHours oh " +
        "WHERE w.room.floor.building.id = :buildingId " +
        "AND w.workstationType = :workstationType " +
        "AND oh.day = :dayOfWeek " +
        "AND oh.startingTime <= :startingTime " +
        "AND oh.endingTime >= :endingTime " +
        "AND w.id NOT IN ( " +
            "SELECT b.workstation.id FROM Booking b " +
            "WHERE b.date = :date " +
            "AND ( " +
            "(:startingTime < b.endingTime AND :endingTime > b.startingTime)" +
            ")" +
        ")"
    )
    List<Workstation> findAvailableWorkstations(@Param("buildingId") Long buildingId,
                                                @Param("workstationType") String workstationType,
                                                @Param("date") LocalDate date,
                                                @Param("startingTime") LocalTime startingTime,
                                                @Param("endingTime") LocalTime endingTime,
                                                @Param("dayOfWeek") DayOfWeek dayOfWeek);

}
