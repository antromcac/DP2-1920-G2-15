
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository {

	Room findById(int id);

	Collection<Room> findAll();

    void delete(Room room);

    void save(Room room);

}