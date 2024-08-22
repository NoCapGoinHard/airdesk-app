package it.airdesk.airdesk_app.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.repository.auth.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
}
