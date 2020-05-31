
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Chip;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ChipService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class ChipController {

	private final ChipService	chipService;
	private final PetService	petService;
	
	private static final String PET_ID = "petId";
	private static final String OWNER_REDIRECT = "redirect:/owners/{ownerId}";
	private static final String	VIEWS_CHIPS_CREATE_OR_UPDATE_FORM	= "chips/createOrUpdateChipForm";


	@Autowired
	public ChipController(final ChipService chipService, final PetService petService) {
		this.chipService = chipService;
		this.petService = petService;
	}

	@GetMapping("/chips/{chipId}")
	public ModelAndView showChip(@PathVariable("chipId") final int chipId) {
		final ModelAndView mav = new ModelAndView("chips/chipDetails");
		mav.addObject(this.chipService.findChipById(chipId));
		return mav;
	}

	@GetMapping("/chips/new")
	public String initCreationForm(@PathVariable("petId") final int petId, final ModelMap model) {
		final Chip chip = new Chip();
		model.put("chip", chip);
		model.put(PET_ID, petId);
		return ChipController.VIEWS_CHIPS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/chips/new")
	public String processCreationForm(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, @Valid final Chip chip, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("chip", chip);
			model.put(PET_ID, petId);
			return ChipController.VIEWS_CHIPS_CREATE_OR_UPDATE_FORM;
		} else {
			Pet pet = this.petService.findPetById(petId);
			pet.setChip(chip);
			this.chipService.saveChip(chip);
			return OWNER_REDIRECT;
		}
	}

	@GetMapping(value = "/chips/{chipId}/edit")
	public String initUpdateForm(@PathVariable("chipId") final int chipId, final ModelMap model) {
		Chip chip = this.chipService.findChipById(chipId);
		model.addAttribute(chip);
		return ChipController.VIEWS_CHIPS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/chips/{chipId}/edit")
	public String processUpdateForm(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, @PathVariable("chipId") final int chipId, @Valid final Chip chip, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("chip", chip);
			model.put(PET_ID, petId);
			return ChipController.VIEWS_CHIPS_CREATE_OR_UPDATE_FORM;
		} else {

			final Chip chipToUpdate = this.chipService.findChipById(chipId);

			chipToUpdate.setSerialNumber(chip.getSerialNumber());
			chipToUpdate.setModel(chip.getModel());
			chipToUpdate.setGeolocatable(chip.getGeolocatable());

			final Pet pet = this.petService.findPetById(chipId);
			pet.setChip(chipToUpdate);
			this.chipService.saveChip(chipToUpdate);

			return OWNER_REDIRECT;
		}
	}

	@GetMapping(value = "/chips/{chipId}/delete")
	public String removeChip(@PathVariable("chipId") final int chipId, final ModelMap model) {
		Chip chip = this.chipService.findChipById(chipId);
		this.chipService.deleteChip(chip);
		model.addAttribute("message", "Chip succefully deleted!");
		return OWNER_REDIRECT;
	}
}
