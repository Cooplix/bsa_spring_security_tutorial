package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class HotelService {
	private final HotelRepository hotelRepository;

	public HotelService(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	public void delete(UUID hotelId) {
		boolean wasDeleted = hotelRepository.delete(hotelId);
		if (!wasDeleted) {
			throw new NoSuchElementException();
		}
	}

	public List<Hotel> getAll() {
		return hotelRepository.getHotels();
	}

	public Hotel save(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	public Hotel getById(UUID hotelId) {
		return hotelRepository.getById(hotelId).orElseThrow();
	}
}
