package org.springframework.samples.petclinic.service;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Disease;
import org.springframework.samples.petclinic.repository.DiseaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
@Service
public class DiseaseService {

	private DiseaseRepository diseaseRepository;

	@Autowired
	public DiseaseService(DiseaseRepository diseaseRepository) {
		this.diseaseRepository = diseaseRepository;
	}

	@Transactional(readOnly = true)
	public Disease findDiseaseById(int id) {
		return diseaseRepository.findById(id);
    }
	

	@Transactional
	@CacheEvict(cacheNames = "AllDiseases",allEntries = true)
	public void saveDisease(Disease disease) {
		this.diseaseRepository.save(disease);
	}

	@Transactional(readOnly = true)
	@Cacheable("AllDiseases")
	public Collection<Disease> findAll() {
		return this.diseaseRepository.findAll();
	}

	public void delete(Disease disease) {
		
		this.diseaseRepository.delete(disease);
		
	}
	
}
