package org.springframework.samples.petclinic.web;

import java.util.DuplicateFormatFlagsException;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookingService;
    
    private static final String VIEWS_ROOMS_CREATE_OR_UPDATE_FORM = "rooms/createOrUpdateRoomForm";

	@Autowired
	public RoomController(RoomService roomService, BookingService bookingService) {
		this.roomService = roomService;
		this.bookingService= bookingService;
		
	}

	@GetMapping("/rooms/roomsList")
	public String findRoom(final ModelMap modelMap) {

		Iterable<Room> rooms = this.roomService.findAll();
		modelMap.addAttribute("rooms", rooms);
		return "rooms/roomsList";
	}

	@GetMapping("/rooms/{roomId}")
	public ModelAndView showRoom(@PathVariable("roomId") final int roomId) {
		if(roomId<=0) {
			return new ModelAndView("exception");
		}
		final ModelAndView mav = new ModelAndView("rooms/roomDetails");
		mav.addObject(this.roomService.findRoomById(roomId));
		return mav;
    }
    
    @GetMapping(value = "/rooms/delete/{roomId}")
	public String deleteDisease(@PathVariable("roomId") int roomId, ModelMap modelMap) {
    	
    	for(Booking b: this.bookingService.findAll()) {
    		if(b.getRoom().getId()==roomId) {
    			this.bookingService.deleteBooking(b);
    		}
    	}
    	
    	if(roomId<=0) {
			return "redirect:/oups";
		}
    	
    	Room room = roomService.findRoomById(roomId);
		this.roomService.delete(room);
	
		modelMap.addAttribute("message", "Room succefully deleted!");
		return "redirect:/rooms/roomsList";
    }
    
    @GetMapping("/rooms/new")
	public String initCreationForm(final ModelMap model) {
		final Room room = new Room();
		model.put("room", room);
		return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/rooms/new")
	public String processCreationForm(@Valid Room room,
			BindingResult result, final ModelMap model) {

		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
		} else {
			room.setFloor(room.getFloor());
			room.setMedicalTeam(room.getMedicalTeam());
			room.setId(room.getId());
			this.roomService.saveRoom(room);

			return "redirect:/rooms/" + room.getId();
		}
    }
    
    @GetMapping(value = "/rooms/{roomId}/edit")
	public String initUpdateForm(@PathVariable("roomId") final int roomId, final ModelMap model) {
		final Room room = this.roomService.findRoomById(roomId);
		model.put("room", room);
		return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/rooms/{roomId}/edit")
	public String processUpdateForm(@Valid Room room, BindingResult result,
			@PathVariable("roomId") final int roomId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
		} else {
			Room roomToUpdate = this.roomService.findRoomById(roomId);
			BeanUtils.copyProperties(room, roomToUpdate, "id");
			try {
				this.roomService.saveRoom(roomToUpdate);
			} catch (DuplicateFormatFlagsException ex) {
				result.rejectValue("name", "duplicate", "already exist");
				return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/rooms/{roomId}";
		}
	}

}
