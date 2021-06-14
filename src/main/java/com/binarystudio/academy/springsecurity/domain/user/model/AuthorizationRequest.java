package com.binarystudio.academy.springsecurity.domain.user.model;

import lombok.Data;

@Data
public class AuthorizationRequest {
	private String username;
	private String password;
}
