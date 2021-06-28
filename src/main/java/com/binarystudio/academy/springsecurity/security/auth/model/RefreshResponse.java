package com.binarystudio.academy.springsecurity.security.auth.model;

import lombok.Data;

@Data
public class RefreshResponse {
	private String accessToken;
	private String refreshToken;

	public static RefreshResponse of(String accessToken, String refreshToken) {
		var response = new RefreshResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		return response;
	}
}
