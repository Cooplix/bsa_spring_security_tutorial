package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("safe/login")
	public AuthResponse login(@RequestBody AuthorizationRequest authorizationRequest) {
		return authService.performLogin(authorizationRequest);
	}

	@PostMapping("safe/register")
	public AuthResponse register(@RequestBody RegistrationRequest registrationRequest) {
		// 1. todo: implement registration
		return authService.createNewUser(registrationRequest);
	}

	@PostMapping("safe/refresh")
	public AuthResponse refreshTokenPair(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		// 2. todo: implement refresh token
		return authService.refreshTokenPair(refreshTokenRequest);
	}

	@PutMapping("safe/forgotten_password")
	public void forgotPasswordRequest(@RequestParam String email) {
		// 6. todo: implement token display for further password update
		authService.displayToken(email);

	}

	@PatchMapping("safe/forgotten_password")
	public AuthResponse forgottenPasswordReplacement(@RequestBody ForgottenPasswordReplacementRequest forgottenPasswordReplacementRequest) {
		// 6. todo: implement password replacement and returning tokens
		return null;
	}

	@PatchMapping("change_password")
	public AuthResponse changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, @RequestParam UUID id) {
		// 5. todo: implement password changing
		//не понял что я тут должен был вернуть
		//новый токен?
		return authService.changePassword(passwordChangeRequest, id);

	}

	@GetMapping("me")
	public User whoAmI(@AuthenticationPrincipal User user) {
		return user;
	}
}
