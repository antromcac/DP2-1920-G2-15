/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Disease;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.DiseaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.samples.petclinic.repository.PetRepository;
@Service
public class DiseaseService {

	@Autowired
	private DiseaseRepository diseaseRepository;

	@Autowired
	private PetRepository petRepository;


@Transactional
public Iterable<Disease> findAll() {
	return this.diseaseRepository.findAll();
}

@Transactional(readOnly = true)	
public Collection<Pet> findPets() throws DataAccessException {
	return this.petRepository.findAll();
}	

@Transactional
public Disease save(Disease d) {		
	Disease saved = this.diseaseRepository.save(d);
	 return saved;
 }

public Pet findOnePet(Pet pet) {
	return this.petRepository.findOnePet(pet);
	
	
}

@Transactional
public Disease findOnebyId(Integer id) {

	return this.diseaseRepository.findOnebyId(id);
}





}
