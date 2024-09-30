package it.airdesk.airdesk_app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;

@Repository
public interface OfficeHoursRepository extends CrudRepository<OfficeHours, Long> {
}
