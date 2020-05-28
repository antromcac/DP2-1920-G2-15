
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Disease;

public interface DiseaseRepository {

	 Disease findById(int id) throws DataAccessException;


	void save(Disease disease) throws DataAccessException;


	 Collection<Disease> findAll() throws DataAccessException;


	void delete(Disease disease);

}