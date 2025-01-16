package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Employee;
import it.airdesk.airdesk_app.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository; // Repository per interagire con il database

    public void save(Employee employee) {
        employeeRepository.save(employee); // Salva il dipendente nel database
    }
}
