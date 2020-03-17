
package org.springframework.samples.petclinic.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Opinion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.OpinionRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OpinionService
 */
@Service
public class OpinionService {

	@Autowired
	private OpinionRepository	opinionRepo;

	@Autowired
	private VetRepository		vetRepo;

	@Autowired
	private OwnerRepository		ownerRepo;

	@Autowired
	private UserRepository		userRepo;


	public OpinionService(final OpinionRepository repository, final VetRepository vetRepo, final OwnerRepository ownerRepo, final UserRepository userRepo) {
		this.opinionRepo = repository;
		this.vetRepo = vetRepo;
		this.ownerRepo = ownerRepo;
		this.userRepo = userRepo;
	}

	public void saveOpinion(@Valid final Opinion opinion) {
		this.opinionRepo.save(opinion);

	}

	public Vet getVetById(final Integer vetId) {
		return this.vetRepo.findById(vetId);
	}

	public Owner getOwnerById(final Integer ownerId) {
		return this.ownerRepo.findById(ownerId);
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		return this.userRepo.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre

	}

	@Transactional
	public Iterable<Opinion> findAll() {
		return this.opinionRepo.findAll();
	}

}