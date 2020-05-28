
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository {

	Room findById(int id) throws DataAccessException;

	Collection<Room> findAll() throws DataAccessException;

    void delete(Room room);

    void save(Room room) throws DataAccessException;

}