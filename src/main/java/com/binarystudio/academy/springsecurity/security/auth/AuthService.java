package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.UserService;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.*;
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
		return AuthResponse.of(jwtProvider.generateToken(userDetails), jwtProvider.refreshToken(userDetails));
	}

	private boolean passwordsDontMatch(String rawPw, String encodedPw) {
		return !passwordEncoder.matches(rawPw, encodedPw);
	}

	public AuthResponse register(RegistrationRequest registrationRequest) {
		var newUser = new User();
		newUser = userService.createNewUserWithoutOAuth(registrationRequest, newUser);
		newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

		return AuthResponse.of(jwtProvider.generateToken(newUser), jwtProvider.refreshToken(newUser));
	}

	public AuthResponse changePassword(PasswordChangeRequest passwordChangeRequest, UUID id) {
		var user = userService.newPassword(id);
		user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		return AuthResponse.of(jwtProvider.generateToken(user), jwtProvider.refreshToken(user));
	}

	public AuthResponse displayToken(String email) {
		var findUserByEmail = userService.loadUserByEmail(email);

		if(findUserByEmail == null) {
			throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid password");
		}
		return AuthResponse.of(jwtProvider.generateToken(findUserByEmail), jwtProvider.refreshToken(findUserByEmail));
	}
	//ексепшени добавлю трішки пізніше
	public AuthResponse refreshTokenPair(RefreshTokenRequest refreshTokenRequest) {
		var user = userService.loadUserByEmail(jwtProvider.getLoginFromToken(refreshTokenRequest.getRefreshToken()));
		return AuthResponse.of(jwtProvider.generateToken(user), jwtProvider.refreshToken(user));
	}

	public AuthResponse forgottenPasswordReplacement(ForgottenPasswordReplacementRequest forgottenPasswordReplacementRequest) {
		var user = userService.loadUserByEmail(jwtProvider.getLoginFromToken(forgottenPasswordReplacementRequest.getToken()));
		user.setPassword(passwordEncoder.encode(forgottenPasswordReplacementRequest.getNewPassword()));
		return AuthResponse.of(jwtProvider.generateToken(user), jwtProvider.refreshToken(user));
	}
}
