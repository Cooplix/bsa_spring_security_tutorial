package com.binarystudio.academy.springsecurity.security.oauth2;

import com.binarystudio.academy.springsecurity.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class SimpleOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SimpleOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		var oAuth2User = super.loadUser(userRequest);
		String email = oAuth2User.getAttribute("email");
		var foundUser = userRepository.findByEmail(email);
		if (foundUser.isPresent()) {
			userRepository.createUserByEmail(email);
			// update user, verify his email, etc since the email is returned from trusted source
		} else {
			//не работает, падает 403 ошибка
			userRepository.createUserWithOAuth(oAuth2User.getAttribute("email"), oAuth2User.getAttribute("password"), oAuth2User.getAttribute("login"), passwordEncoder);
			// 1. todo: register the user when he wasn't found
		}
		return oAuth2User;
	}
}
