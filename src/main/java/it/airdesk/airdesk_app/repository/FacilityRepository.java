package it.airdesk.airdesk_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Facility;
@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long>{
    
}
