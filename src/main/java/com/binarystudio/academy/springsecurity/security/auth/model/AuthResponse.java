package com.binarystudio.academy.springsecurity.security.auth.model;

import lombok.Data;

@Data
public class AuthResponse {
	private String accessToken;
	// 2 todo: add refresh token
	private String refreshToken;

	public static AuthResponse of(String token, String refreshToken) {
		AuthResponse response = new AuthResponse();
		response.setAccessToken(token);
		response.setRefreshToken(refreshToken);
		return response;
	}
}
