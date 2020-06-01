package org.springframework.samples.petclinic.web.endToEnd;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ChipControllerE2ETest {
	
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_CHIP_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/new", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("chip"))
				.andExpect(view().name("chips/createOrUpdateChipForm"));
	}
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
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
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
    @Test
    void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("chip"))
				.andExpect(model().attribute("chip", hasProperty("serialNumber", is("1"))))
				.andExpect(model().attribute("chip", hasProperty("model", is("model1"))))
				.andExpect(model().attribute("chip", hasProperty("geolocatable", is(true))))
				.andExpect(view().name("chips/createOrUpdateChipForm"));
	}
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
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
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
    @Test
	void testInitShowForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("chip"))
		.andExpect(view().name("chips/chipDetails"));
	}
	
	@WithMockUser(username = "spring", authorities = { "veterinarian"})
    @Test
	void testProcessDeleteChipSuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/chips/{chipId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_CHIP_ID))
				.andExpect(status().is3xxRedirection()); //Poner assertion hacia la vista
	}
}
