package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import com.binarystudio.academy.springsecurity.security.auth.model.PasswordChangeRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.RegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
	private final List<User> users = new ArrayList<>();

	public UserRepository(PasswordEncoder passwordEncoder) {
		var regularUser = new User();
		regularUser.setUsername("regular");
		regularUser.setEmail("regular@mail.com");
		regularUser.setId(UUID.randomUUID());
		regularUser.setPassword(passwordEncoder.encode("password"));
		regularUser.setAuthorities(Set.of(UserRole.USER));
		this.users.add(regularUser);

		var adminUser = new User();
		adminUser.setUsername("privileged");
		adminUser.setEmail("privileged@mail.com");
		adminUser.setId(UUID.randomUUID());
		adminUser.setPassword(passwordEncoder.encode("password"));
		adminUser.setAuthorities(Set.of(UserRole.ADMIN));
		this.users.add(adminUser);
	}

	public User change( UUID id) {
		var user = users.stream().filter(u -> u.getId().equals(id)).findAny().orElse(null);

		return user;
	}

	public Optional<User> findByUsername(String username) {
		return users.stream()
				.filter(user -> user.getUsername().equals(username))
				.findAny();
	}

	public Optional<User> findByEmail(String email) {
		return users.stream()
				.filter(user -> user.getEmail().equals(email))
				.findAny();
	}

	public List<User> findUsers() {
		return Collections.unmodifiableList(users);
	}

	public void createUserByEmail(String email) {
		var createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setUsername(email);
		createdUser.setAuthorities(Set.of(UserRole.USER));
		users.add(createdUser);
	}

	public Optional<User> findByUserId(UUID id) {
		return users.stream()
				.filter(user -> user.getId().equals(id))
				.findAny();
	}


	public User createUser(RegistrationRequest registrationRequest, User newUser) {
		newUser.setUsername(registrationRequest.getLogin());
		newUser.setEmail(registrationRequest.getEmail());
		newUser.setId(UUID.randomUUID());
		newUser.setAuthorities(Set.of(UserRole.USER));
		users.add(newUser);
		return newUser;
	}
}
