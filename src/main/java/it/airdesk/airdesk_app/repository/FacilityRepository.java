package it.airdesk.airdesk_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Company;
import it.airdesk.airdesk_app.model.Facility;
@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long>{

    @Query("SELECT f FROM Facility f JOIN f.buildings b WHERE LOWER(b.address.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    public List<Facility> findByBuildingCityContainingIgnoreCase(@Param("city") String city); //it will search in the embedded addresses for the facilities

    @Query("SELECT fc.id, COUNT(w.id)" +
            "FROM Facility fc " +
            "JOIN fc.buildings b " +
            "JOIN b.floors f " +
            "JOIN f.rooms r " +
            "JOIN r.workstations w " +
            "GROUP BY fc.id ")
    public List<Object[]> findWorkstationCountByFacility();


    @Query("SELECT f FROM Facility f JOIN f.companies c WHERE c.id = :companyId")
    List<Facility> findByCompanyId(@Param("companyId") Long companyId);
}
