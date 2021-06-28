package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
		return null;
	}

	@PostMapping("safe/refresh")
	public RefreshResponse refreshTokenPair(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		// 2. todo: implement refresh token
		return null;
	}

	@PutMapping("safe/forgotten_password")
	public void forgotPasswordRequest(@RequestParam String email) {
		// 7. todo: implement token display for further password update
	}

	@PatchMapping("safe/forgotten_password")
	public RefreshResponse forgottenPasswordReplacement(@RequestBody ForgottenPasswordReplacementRequest forgottenPasswordReplacementRequest) {
		// 7. todo: implement password replacement and returning tokens
		return null;
	}

	@PatchMapping("change_password")
	public void changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
		// 6. todo: implement password changing
	}

	@GetMapping("me")
	public User whoAmI(@AuthenticationPrincipal User user) {
		return user;
	}
}
