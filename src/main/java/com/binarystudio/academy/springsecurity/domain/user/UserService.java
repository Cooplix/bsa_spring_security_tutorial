package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.PasswordChangeRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
	}

	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public List<User> getAll() {
		return userRepository.findUsers();
	}

	public User newPassword(UUID id) {
		return userRepository.change(id);
	}

	public User addNewUser(RegistrationRequest registrationRequest, User newUser) {
		return userRepository.createUser(registrationRequest, newUser);
	}
}
