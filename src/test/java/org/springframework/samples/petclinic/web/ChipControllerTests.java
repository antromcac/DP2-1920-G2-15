
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Chip;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ChipService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = ChipController.class, 
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class ChipControllerTests {

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_CHIP_ID = 1;
	
	@MockBean
	private PetService petService;
        
    @MockBean
	private ChipService chipService;

	@Autowired
	private MockMvc mockMvc;
	
	private Chip testChip;
	
	private Pet testPet;
	
	@BeforeEach
	void setup() {
		
		testChip = new Chip();
		testChip.setId(TEST_CHIP_ID);
		testChip.setSerialNumber("serialNumber");
		testChip.setModel("model");
		testChip.setGeolocatable(true);
		given(this.chipService.findChipById(TEST_CHIP_ID)).willReturn(testChip);
		testPet = new Pet();
		testPet.setName("Jorge");
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(testPet);
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/new", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("chip"))
				.andExpect(view().name("chips/createOrUpdateChipForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/chips/new", TEST_OWNER_ID, TEST_PET_ID)
				.with(csrf())
				.param("serialNumber", "123")
				.param("model", "model123")
				.param("geolocatable", "true"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/chips/new", TEST_OWNER_ID, TEST_PET_ID)
				.with(csrf())
				.param("serialNumber", "123")
				.param("geolocatable", "true"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(model().attributeHasErrors("chip"))
		.andExpect(model().attributeHasFieldErrors("chip", "model"))
		.andExpect(view().name("chips/createOrUpdateChipForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
    @Test
    void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("chip"))
				.andExpect(model().attribute("chip", hasProperty("serialNumber", is("serialNumber"))))
				.andExpect(model().attribute("chip", hasProperty("model", is("model"))))
				.andExpect(model().attribute("chip", hasProperty("geolocatable", is(true))))
				.andExpect(view().name("chips/createOrUpdateChipForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/chips/{chipId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID)
				.with(csrf())
				.param("serialNumber", "123")
				.param("model", "model123")
				.param("geolocatable", "true"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring", authorities = "veterinarian")
	@Test
	void testProcessUpdateHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/chips/{chipId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID)
				.with(csrf())
				.param("serialNumber", "123")
				.param("model", "")
				.param("geolocatable", "true"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(model().attributeHasErrors("chip"))
		.andExpect(model().attributeHasFieldErrors("chip", "model"))
		.andExpect(view().name("chips/createOrUpdateChipForm"));
	}

	@WithMockUser(value = "spring", authorities = "veterinarian")
    @Test
	void testInitShowForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("chip"))
		.andExpect(view().name("chips/chipDetails"));
	}
    
	@WithMockUser(value = "spring", authorities = "veterinarian")
    @Test
	void testProcessDeleteChipSuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
				.andExpect(status().is3xxRedirection());
	}

}
