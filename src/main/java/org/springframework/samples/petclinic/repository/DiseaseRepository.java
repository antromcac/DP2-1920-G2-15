
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Disease;

public interface DiseaseRepository {

	 Disease findById(int id);


	void save(Disease disease);


	 Collection<Disease> findAll();


	void delete(Disease disease);

}