
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Chip;

public interface ChipRepository {

	<Optional> Chip findById(int id) throws DataAccessException;
}