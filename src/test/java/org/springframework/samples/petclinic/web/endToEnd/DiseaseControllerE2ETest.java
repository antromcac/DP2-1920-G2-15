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
import org.springframework.samples.petclinic.service.DiseaseService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class DiseaseControllerE2ETest {

	private static final int TEST_PET_ID = 1;
	private static final int TEST_DISEASE_ID = 1;
	private static final int TEST_DISEASE_ID_NEGATIVE = -1;

	@Autowired
	private MockMvc mockMvc;
    
    
	@Autowired
	private DiseaseService diseaseService;

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testListDisease() throws Exception {
		mockMvc.perform(get("/diseases/diseasesList")).andExpect(status().isOk())
				.andExpect(model().attributeExists("diseases")).andExpect(view().name("diseases/diseasesList"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testNotListDisease() throws Exception {
		mockMvc.perform(get("/diseases/diseasesList")).andExpect(status().is4xxClientError())
				.andExpect(status().reason("Forbidden"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/diseases/new/{petId}", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("disease"))
				.andExpect(view().name("diseases/createOrUpdateDiseaseForm"))
				.andExpect(model().attributeExists("disease"));
	}

	@WithMockUser(username = "admin1", authorities = { "admin" })
	@Test
	void testInitCreationFormFail() throws Exception {
		mockMvc.perform(get("/diseases/new/{petId}", TEST_PET_ID)).andExpect(status().is(403))
				.andExpect(status().reason("Forbidden"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
        Integer i = diseaseService.findAll().size();
		mockMvc.perform(post("/diseases/new/{petId}", TEST_PET_ID).requestAttr("petId", TEST_PET_ID).with(csrf())
				.param("symptoms", "Mareos, vomitos y diarrea...").param("severity", "LOW")
				.param("cure", "Unas pastillas")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/diseases/"+(i+1)));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/diseases/new/{petId}", TEST_PET_ID).requestAttr("petId", TEST_PET_ID).with(csrf())
				.param("symptoms", "Mareos, vomitos y diarrea...").param("severity", "En duda")
				.param("cure", "Unas pastillas")).andExpect(model().attributeHasErrors("disease"))
				.andExpect(status().is(200)).andExpect(view().name("diseases/createOrUpdateDiseaseForm"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testShowDisease() throws Exception {

		mockMvc.perform(get("/diseases/{diseaseId}", 2).requestAttr("diseaseId", 2))
				.andExpect(status().isOk())
				.andExpect(model().attribute("disease", hasProperty("cure", is("tengo el hambre"))))
				.andExpect(model().attribute("disease", hasProperty("severity", is("MEDIUM"))))
				.andExpect(model().attribute("disease", hasProperty("symptoms", is("la vas a espichar"))))
				.andExpect(model().attribute("disease", hasProperty("pet")))
				.andExpect(model().attributeExists("disease")).andExpect(view().name("diseases/diseaseDetails"));

	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testShowDiseaseError() throws Exception {
		mockMvc.perform(get("/diseases/{diseaseId}", TEST_DISEASE_ID_NEGATIVE).requestAttr("diseaseId", TEST_DISEASE_ID_NEGATIVE)).
		andExpect(status().is2xxSuccessful())
				.andExpect(view().name("exception"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testInitUpdateDiseaseForm() throws Exception {

		mockMvc.perform(get("/diseases/{diseaseId}/edit", TEST_DISEASE_ID).requestAttr("diseaseId", 1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("disease"))
				.andExpect(model().attribute("disease", hasProperty("cure", is("malisimo de la muerte"))))
				.andExpect(model().attribute("disease", hasProperty("severity", is("LOW"))))
				.andExpect(model().attribute("disease", hasProperty("symptoms", is("compra paracetamol"))))
				.andExpect(model().attribute("disease", hasProperty("pet")))
				.andExpect(view().name("diseases/createOrUpdateDiseaseForm"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testProcessUpdateDiseaseFormSuccess() throws Exception {
		mockMvc.perform(post("/diseases/{diseaseId}/edit", TEST_DISEASE_ID).with(csrf()).param("cure", "Comida sana")
				.param("severity", "HIGH").param("symptoms", "Mucho dolor de barriga"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/diseases/{diseaseId}"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testProcessUpdateDiseaseFormHasErrors() throws Exception {
		mockMvc.perform(post("/diseases/{diseaseId}/edit", TEST_DISEASE_ID).with(csrf()).param("cure", "Comida sana")
				.param("severity", "BAJO").param("symptoms", "Dolor de barriga")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("disease"))
				.andExpect(model().attributeHasFieldErrors("disease", "severity"))
				.andExpect(view().name("diseases/createOrUpdateDiseaseForm"));
	}

	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testDelete() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/diseases/delete/{diseaseId}", 3).requestAttr("id","3"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/diseases/diseasesList"));

	}

	
	@WithMockUser(username = "vet1", authorities = { "veterinarian" })
	@Test
	void testDeleteIncorrectId() throws Exception {
		mockMvc.perform(get("/diseases/delete/{diseaseId}", TEST_DISEASE_ID_NEGATIVE).requestAttr("diseaseId", TEST_DISEASE_ID_NEGATIVE))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}
}
