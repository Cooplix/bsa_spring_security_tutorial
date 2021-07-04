package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import com.binarystudio.academy.springsecurity.domain.user.UserRepository;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotelService {
	private final HotelRepository hotelRepository;
	private final UserRepository userRepository;

	@Autowired
	public HotelService(HotelRepository hotelRepository, UserRepository userRepository) {
		this.hotelRepository = hotelRepository;
		this.userRepository = userRepository;
	}

	public void delete(UUID hotelId, User user) {
		if(checkOwnerId(hotelId, hotelId) || checkUserRole(user)) {
			boolean wasDeleted = hotelRepository.delete(hotelId);
			if (!wasDeleted) {
				throw new NoSuchElementException();
			}
		}
	}

	public List<Hotel> getAll() {
		return hotelRepository.getHotels();
	}

	public Hotel update(Hotel hotel, User user) {
		// 4. todo: only the owner of the hotel or admin may update the hotel
		if(checkOwnerId(hotel.getId(), user.getId()) || checkUserRole(user)) {
			getById(hotel.getId());
			return hotelRepository.save(hotel);
		}
		throw new AccessDeniedException("Permission denied, you are not owner of this Hotel");
	}

	public Hotel create(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	public Hotel getById(UUID hotelId) {
		return hotelRepository.getById(hotelId).orElseThrow();
	}

	public boolean checkOwnerId(UUID hotelId, UUID userId) {
		var hotel = hotelRepository.getById(hotelId).orElseThrow();
		return hotel.getOwnerId().equals(userId);
	}

	public boolean checkUserRole(User user) {
		return user.getAuthorities().contains(UserRole.ADMIN);
	}
}
