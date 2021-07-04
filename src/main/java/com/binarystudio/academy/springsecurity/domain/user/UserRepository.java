package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import com.binarystudio.academy.springsecurity.security.auth.model.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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

	public User changeUserPassword( UUID id) {

		return users.stream().filter(u -> u.getId().equals(id)).findAny().orElse(null);
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

	public User createUserByEmail(String email) {
		var createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setUsername(email);
		createdUser.setAuthorities(Set.of(UserRole.USER));
		users.add(createdUser);
		return createdUser;
	}

	public Optional<User> findByUserId(UUID id) {
		return users.stream()
				.filter(user -> user.getId().equals(id))
				.findAny();
	}


	public User createUser(RegistrationRequest registrationRequest, User newUser) {
		if(findByEmail(registrationRequest.getEmail()).isPresent() ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + registrationRequest.getEmail()  + " already exist in DB");
		}

		if(findByUsername(registrationRequest.getLogin()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login: " + registrationRequest.getLogin()  +  "already exist in DB");
		}

		newUser.setUsername(registrationRequest.getLogin());
		newUser.setEmail(registrationRequest.getEmail());
		newUser.setId(UUID.randomUUID());
		newUser.setAuthorities(Set.of(UserRole.USER));
		users.add(newUser);
		return newUser;
	}

	public User createUserWithOAuth(String email, String password, String login, PasswordEncoder passwordEncoder) {
		var userOAuth = new User();
		userOAuth.setUsername(login);
		userOAuth.setEmail(email);
		userOAuth.setId(UUID.randomUUID());
		userOAuth.setPassword(passwordEncoder.encode(password));
		userOAuth.setAuthorities(Set.of(UserRole.ADMIN));
		this.users.add(userOAuth);
		return userOAuth;
	}
}
