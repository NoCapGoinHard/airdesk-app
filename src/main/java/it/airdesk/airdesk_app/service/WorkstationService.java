package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.repository.WorkstationRepository;

@Service
public class WorkstationService {
        
    @Autowired
    private WorkstationRepository workstationRepository;
}
