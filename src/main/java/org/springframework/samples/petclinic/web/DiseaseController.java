package org.springframework.samples.petclinic.web;

import java.util.DuplicateFormatFlagsException;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Disease;
import org.springframework.samples.petclinic.service.DiseaseService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DiseaseController {

	private final DiseaseService diseaseService;
	private final PetService petService;

	private static final String VIEWS_DISEASES_CREATE_OR_UPDATE_FORM = "diseases/createOrUpdateDiseaseForm";

	@Autowired
	public DiseaseController(DiseaseService diseaseService, final PetService petService) {
		this.diseaseService = diseaseService;
		this.petService = petService;
	}

	@GetMapping("/diseases/diseasesList")
	public String findDiseases(final ModelMap modelMap) {

		Iterable<Disease> diseases = this.diseaseService.findAll();
		modelMap.addAttribute("diseases", diseases);
		return "diseases/diseasesList";
	}

	@GetMapping("/diseases/{diseaseId}")
	public ModelAndView showDisease(@PathVariable("diseaseId") final int diseaseId) {
		
		if(diseaseId<=0) {
			return new ModelAndView("exception");
		}
		final ModelAndView mav = new ModelAndView("diseases/diseaseDetails");
		mav.addObject(this.diseaseService.findDiseaseById(diseaseId));
		return mav;
	}

	@GetMapping("/diseases/new/{petId}")
	public String initCreationForm(@PathVariable("petId") final int petId, final ModelMap model) {
		final Disease disease = new Disease();
		model.put("disease", disease);
		model.put("petId", petId);
		return VIEWS_DISEASES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/diseases/new/{petId}")
	public String processCreationForm(@PathVariable("petId") final int petId, @Valid Disease disease,
			BindingResult result, final ModelMap model) {

		if (result.hasErrors()) {
			model.put("disease", disease);
			model.put("petId", petId);
			return VIEWS_DISEASES_CREATE_OR_UPDATE_FORM;
		} else {
			disease.setSymptoms(disease.getSymptoms());
			disease.setCure(disease.getCure());
			disease.setSeverity(disease.getSeverity());
			disease.setPet(this.petService.findPetById(petId));
			disease.setId(disease.getId());
			this.diseaseService.saveDisease(disease);

			return "redirect:/diseases/" + disease.getId();
		}
	}

	
	@GetMapping(value = "/diseases/{diseaseId}/edit")
	public String initUpdateForm(@PathVariable("diseaseId") final int diseaseId, final ModelMap model) {
		final Disease disease = this.diseaseService.findDiseaseById(diseaseId);
		model.put("disease", disease);
		return VIEWS_DISEASES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/diseases/{diseaseId}/edit")
	public String processUpdateForm(@Valid Disease disease, BindingResult result,
			@PathVariable("diseaseId") final int diseaseId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("disease", disease);
			return VIEWS_DISEASES_CREATE_OR_UPDATE_FORM;
		} else {
			Disease diseaseToUpdate = this.diseaseService.findDiseaseById(diseaseId);
			BeanUtils.copyProperties(disease, diseaseToUpdate, "id", "pet");
			try {
				this.diseaseService.saveDisease(diseaseToUpdate);
			} catch (DuplicateFormatFlagsException ex) {
				result.rejectValue("name", "duplicate", "already exist");
				return VIEWS_DISEASES_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/diseases/{diseaseId}";
		}
	}

	@GetMapping(value = "/diseases/delete/{diseaseId}")
	public String deleteDisease(@PathVariable("diseaseId") int diseaseId, ModelMap modelMap) {

		if(diseaseId<=0) {
			return "redirect:/oups";
		}
		Disease disease = diseaseService.findDiseaseById(diseaseId);
		this.diseaseService.delete(disease);
		modelMap.addAttribute("message", "Disease succefully deleted!");
		return "redirect:/diseases/diseasesList";
	}

}