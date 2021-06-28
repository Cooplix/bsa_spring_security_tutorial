package com.binarystudio.academy.springsecurity.security.auth.model;

import lombok.Data;

@Data
public class AuthResponse {
	private String accessToken;

	public static AuthResponse of(String token) {
		AuthResponse response = new AuthResponse();
		response.setAccessToken(token);
		return response;
	}
}
