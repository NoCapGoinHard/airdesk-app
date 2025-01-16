package it.airdesk.airdesk_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.airdesk.airdesk_app.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
