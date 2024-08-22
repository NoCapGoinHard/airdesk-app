package it.airdesk.airdesk_app.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Facility;
@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long>{

    public List<Facility> findByAddress_CityContainingIgnoreCase(String city); //it will search in the embedded addresses for the facilities
}
