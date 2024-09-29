package it.airdesk.airdesk_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Room;
import it.airdesk.airdesk_app.repository.RoomRepository;

@Service
public class RoomService {
        
    @Autowired
    private RoomRepository roomRepository;

    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public void save(Room room) {
        roomRepository.save(room);
    }
}
