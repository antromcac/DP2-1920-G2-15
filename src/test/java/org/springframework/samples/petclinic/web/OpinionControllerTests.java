
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Opinion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OpinionService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * OpinionControllerTests
 */
@WebMvcTest(controllers = OpinionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class OpinionControllerTests {

	private static final int	TEST_OPINION_ID	= 1;

	private static final String	TEST_USER_ID	= "owner1";

	private static final int	TEST_VET_ID		= 1;

	@Autowired
	private OpinionController	opinionController;

	@MockBean
	private OpinionService		opinionService;

	@MockBean
	private UserService			userService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;

	private Optional<Opinion>	opinion;

	private Vet					vetMock;


	@BeforeEach
	void setup() {
		User userTest = new User();
		Opinion op = new Opinion();
		userTest.setUsername(OpinionControllerTests.TEST_USER_ID);
		this.vetMock = new Vet();
		this.vetMock.setId(OpinionControllerTests.TEST_VET_ID);
		op.setId(OpinionControllerTests.TEST_OPINION_ID);
		op.setComentary("Muy buen servicio");
		op.setPuntuation(5);
		op.setUser(userTest);
		op.setVet(this.vetMock);
		op.setDate(LocalDateTime.of(2020, 01, 04, 00, 00, 00));
		this.opinion = Optional.of(op);
		BDDMockito.given(this.opinionService.findOpinionById(OpinionControllerTests.TEST_OPINION_ID)).willReturn(this.opinion);

	}

	//  Test del Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/new/{vetId}", OpinionControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("opinion"))
			.andExpect(MockMvcResultMatchers.view().name("opinions/createOrUpdateOpinions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/opinions/new/{vetId}", OpinionControllerTests.TEST_VET_ID).param("puntuation", "4").param("comentary", "Successful test").with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/opinions/new/{vetId}", OpinionControllerTests.TEST_VET_ID)				// Tanto date como user se asignan en el controller por lo que no se pueden asignar incorrectamente en el formulario.
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("opinion"))	// Comentary es un atributo que puede ir vacío.
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("opinion", "puntuation")).andExpect(MockMvcResultMatchers.view().name("opinions/createOrUpdateOpinions"));
	}

	//	Test del List

	@WithMockUser(value = "spring")
	@Test
	void testInitListForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("opinions"))
			.andExpect(MockMvcResultMatchers.view().name("/opinions/listOpinions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessListFormSuccess() throws Exception {
		List<Opinion> itList = new ArrayList<>();
		itList.add(this.opinion.get());
		Iterable<Opinion> it = itList;
		BDDMockito.given(this.opinionService.findAll()).willReturn(it);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/list")).andExpect(MockMvcResultMatchers.model().attribute("opinions", it)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/opinions/listOpinions"));
	}

	@WithMockUser(value = "owner1")
	@Test
	void testProcessListFormMine() throws Exception {
		List<Opinion> itList = new ArrayList<>();
		itList.add(this.opinion.get());
		Iterable<Opinion> it = itList;
		BDDMockito.given(this.opinionService.findAllMine(OpinionControllerTests.TEST_USER_ID)).willReturn(it);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/listMine")).andExpect(MockMvcResultMatchers.model().attribute("opinions", it)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/opinions/listOpinions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessListFormNoFound() throws Exception {
		List<Opinion> itList = new ArrayList<>();
		Iterable<Opinion> it = itList;
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("opinions", it))
			.andExpect(MockMvcResultMatchers.view().name("/opinions/listOpinions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessListMineFormNoFound() throws Exception {
		List<Opinion> itList = new ArrayList<>();
		Iterable<Opinion> it = itList;
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/listMine")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("opinions", it))
			.andExpect(MockMvcResultMatchers.view().name("/opinions/listOpinions"));
	}

	//  Test del Update

	@WithMockUser(value = "owner1")
	@Test
	void testInitEditOpinionForm() throws Exception {
		User userMock = new User();
		userMock.setUsername(OpinionControllerTests.TEST_USER_ID);
		userMock.setEnabled(false);
		userMock.setPassword(null);
		this.vetMock.setId(OpinionControllerTests.TEST_VET_ID);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/edit/{opinionId}", OpinionControllerTests.TEST_OPINION_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("opinion"))
			.andExpect(MockMvcResultMatchers.model().attribute("opinion", Matchers.hasProperty("user", Matchers.is(userMock)))).andExpect(MockMvcResultMatchers.model().attribute("opinion", Matchers.hasProperty("puntuation", Matchers.is(5))))
			.andExpect(MockMvcResultMatchers.model().attribute("opinion", Matchers.hasProperty("comentary", Matchers.is("Muy buen servicio"))))
			.andExpect(MockMvcResultMatchers.model().attribute("opinion", Matchers.hasProperty("date", Matchers.is(LocalDateTime.of(2020, 01, 04, 00, 00, 00)))))
			.andExpect(MockMvcResultMatchers.model().attribute("opinion", Matchers.hasProperty("vet", Matchers.is(this.vetMock)))).andExpect(MockMvcResultMatchers.view().name("opinions/createOrUpdateOpinions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessEditOpinionFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/opinions/edit/{opinionId}", OpinionControllerTests.TEST_OPINION_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("comentary", "Pues no está tan bien.").param("puntuation", "5"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/opinions/listMine"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessEditOpinionFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/opinions/edit/{opinionId}", OpinionControllerTests.TEST_OPINION_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("comentary", "Pues no está tan bien."))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("opinion")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("opinion", "puntuation"))
			.andExpect(MockMvcResultMatchers.view().name("opinions/createOrUpdateOpinions"));
	}

	// Test del delete

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteOpinionSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/{opinionId}/delete", OpinionControllerTests.TEST_OPINION_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/opinions/listMine"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteOpinionFail() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/opinions/{opinionId}/delete", 2)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/opinions/listMine"));
	}
}
