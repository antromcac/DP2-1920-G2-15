package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

	private RoomRepository roomRepository;

	@Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Transactional(readOnly = true)
	public Room findRoomById(int id) {
		return roomRepository.findById(id);
    }

	@Transactional
	public Collection<Room> findAll() {
		return this.roomRepository.findAll();
    }
    
    public void delete(Room room) {
		
	this.roomRepository.delete(room);
		
	}
    
    @Transactional
	public void saveRoom(Room room){
		this.roomRepository.save(room);
	}
}