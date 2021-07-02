package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.UserService;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.AuthResponse;
import com.binarystudio.academy.springsecurity.security.auth.model.AuthorizationRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.PasswordChangeRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.RegistrationRequest;
import com.binarystudio.academy.springsecurity.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
	private final UserService userService;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthResponse performLogin(AuthorizationRequest authorizationRequest) {
		var userDetails = userService.loadUserByUsername(authorizationRequest.getUsername());
		if (passwordsDontMatch(authorizationRequest.getPassword(), userDetails.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
		}
		// 2. todo: auth and refresh token are given to user
		return AuthResponse.of(jwtProvider.generateToken(userDetails));
	}

	private boolean passwordsDontMatch(String rawPw, String encodedPw) {
		return !passwordEncoder.matches(rawPw, encodedPw);
	}

	public AuthResponse createNewUser(RegistrationRequest registrationRequest) {
		var newUser = new User();
		newUser = userService.addNewUser(registrationRequest, newUser);
		newUser.setPassword(registrationRequest.getPassword());

		return AuthResponse.of(jwtProvider.generateToken(newUser));
	}

	public String changePassword(PasswordChangeRequest passwordChangeRequest, UUID id) {
		var user = userService.newPassword(id);
		//user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		user.setPassword(passwordChangeRequest.getNewPassword());
		return jwtProvider.generateToken(user);
	}
}
