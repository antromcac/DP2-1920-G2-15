
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

	private BookingRepository bookingRepository;


	@Autowired
	public BookingService(final BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;
	}

	@Transactional
	@ReadOnlyProperty
	@Cacheable("bookings")
	public Iterable<Booking> findAll() {
		return this.bookingRepository.findAll();
	}

	@Transactional
	@CacheEvict(cacheNames = "bookings", allEntries = true)
	public void save(final Booking booking) {
		this.bookingRepository.save(booking);
	}

	@Transactional
	@ReadOnlyProperty
	public Optional<Booking> findById(final Integer id) {
		return this.bookingRepository.findById(id);
	}

	@Transactional
	@CacheEvict(cacheNames = "bookings", allEntries = true)
	public void deleteBooking(final Booking book) {
		this.bookingRepository.delete(book);
	}
}
