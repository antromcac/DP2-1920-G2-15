package org.springframework.samples.petclinic.web;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.hamcrest.Matchers.hasProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.RoomService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.Collection;





@WebMvcTest(controllers=RoomController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),excludeAutoConfiguration= SecurityConfiguration.class)
public class RoomControllerTests {

	private static final int TEST_ROOM_ID_POSITIVE = 300;
	private static final int TEST_ROOM_ID_NEGATIVE = -300;
	

	@MockBean
	private RoomService	roomService;

	
	@Mock 	
	private Room room;
	
	@Autowired
	private MockMvc mockMvc;
	
	

	
	@BeforeEach	
	void setup() {

		room = new Room();
		room.setId(TEST_ROOM_ID_POSITIVE);
		room.setFloor(2);
		Collection<String> medicalTeam = new ArrayList<>() ;
		medicalTeam.add("bisturi");
		room.setMedicalTeam(medicalTeam);
		room.setName("Quirofano");
		
		room.getId();
		room.getFloor();
		room.getMedicalTeam();
		room.getName();
		
		given(this.roomService.findRoomById(TEST_ROOM_ID_POSITIVE)).willReturn(room);
		
		
		
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/rooms/new")).andExpect(status().isOk()).andExpect(
				model().attributeExists("room"))
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/rooms/new").param("id","300")
							.with(csrf())
							.param("floor", "4")
							.param("medicalTeam", "gasas")
							.param("name","Quirofano"))
				.andExpect(status().is3xxRedirection()).
				andExpect(view().name("redirect:/rooms/" + room.getId()));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/rooms/new",TEST_ROOM_ID_POSITIVE)
							.with(csrf())
							.param("floor", "pero si esto es un integer"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("room"))
				.andExpect(model().attributeHasFieldErrors("room", "floor"))
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}


//bien
    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/rooms/{roomId}/edit", TEST_ROOM_ID_POSITIVE))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("room"))
		.andExpect(model().attribute("room", hasProperty("medicalTeam", is(room.getMedicalTeam()))))
		.andExpect(model().attribute("room", hasProperty("name", is("Quirofano"))))		
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
    
        
       

    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormSuccess() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/edit", TEST_ROOM_ID_POSITIVE)
							.with(csrf())
							.param("floor", "2")
							.param("medicalTeam","bisturi")
							.param("name", "Quirofano"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rooms/{roomId}"));
	}



    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateRoomFormHasErrors() throws Exception {
		mockMvc.perform(post("/rooms/{roomId}/edit", TEST_ROOM_ID_POSITIVE)
							.with(csrf())
							.param("medicalTeam","bisturi")
							.param("name", "Quirofano"))
				.andExpect(model().attributeHasErrors("room"))
				.andExpect(model().attributeHasFieldErrors("room", "floor"))
				.andExpect(view().name("rooms/createOrUpdateRoomForm"));
	}
   

    @WithMockUser(value = "spring")
    @Test
	void testShowRoom() throws Exception {
	mockMvc.perform(get("/rooms/{roomId}", TEST_ROOM_ID_POSITIVE))
	.andExpect(model().attribute("room", hasProperty("floor", is(room.getFloor()))))
	.andExpect(model().attribute("room", hasProperty("medicalTeam", is(room.getMedicalTeam()))))
	.andExpect(model().attribute("room", hasProperty("name", is("Quirofano"))))			
	.andExpect(view().name("rooms/roomDetails"));

	}
    
    @WithMockUser(value = "spring")
    @Test
    void testShowRoomNegative() throws Exception {
		mockMvc.perform(get("/rooms/{roomId}",  TEST_ROOM_ID_NEGATIVE))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("exception"));
	}
    
    @WithMockUser(value = "spring")
  	@Test
  	void testListRoom() throws Exception {
  		mockMvc.perform(get("/rooms/roomsList")).andExpect(status().isOk()).
  		andExpect(MockMvcResultMatchers.model().attributeExists("rooms"));
	  }
    


	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception{
	  this.mockMvc.perform(MockMvcRequestBuilders
	  .get("/rooms/delete/{roomId}", TEST_ROOM_ID_POSITIVE))
	  .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	  .andExpect(MockMvcResultMatchers.view().name("redirect:/rooms/roomsList"));
  
	  }
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteIncorrectId() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete/{roomId}", TEST_ROOM_ID_NEGATIVE))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
}
	}


