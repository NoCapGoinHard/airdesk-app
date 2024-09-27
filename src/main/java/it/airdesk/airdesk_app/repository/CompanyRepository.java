package it.airdesk.airdesk_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.airdesk.airdesk_app.model.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

    public Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    public List<Company> findByNameLike(String name);
}